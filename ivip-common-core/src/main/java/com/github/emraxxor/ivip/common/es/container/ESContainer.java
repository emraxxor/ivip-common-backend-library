package com.github.emraxxor.ivip.common.es.container;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ESExtendedDataElement;
import com.github.emraxxor.ivip.common.es.ESOperationType;
import com.github.emraxxor.ivip.common.es.type.ESSimpleResquestElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/*
 * The "EsContainer" component is a component that implements an alternative storage method.
 *
 * @author Attila Barna
 * @category infovip.core.data.manager
 */
@ConditionalOnProperty(value = "ivip.elasticsearch.enabled", havingValue = "true")
@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ESContainer<T extends ESExtendedDataElement<?>> extends Thread implements ESContainerIF<T> {

    final RestHighLevelClient client;

    final LinkedBlockingQueue<T> documentQueue = new LinkedBlockingQueue<>(50000);

    private final ReentrantLock lock = new ReentrantLock();

    BulkRequest bulkRequest;

    int bulkCounter = 0;

    @Value("${ivip.elasticsearch.bulk.size:5000}")
    Integer bulkSize;

    @Value("${ivip.elasticsearch.bulk.wait:15000}")
    Long bulkWaitTime;

    Boolean isRunning = true;

    @PostConstruct
    public void postConstruct() {
        log.info("Creating default container for elasticsearch");
        preapreRequest();
        start();
    }

    /**
     * Prepares a request
     */
    public void preapreRequest() {
        bulkRequest = new BulkRequest();
    }

    @PreDestroy
    public void preDestroy() {
        terminate();
    }

    @Override
    public void run() {
        while (Boolean.TRUE.equals(isRunning)) {
            T data = null;
            while (data == null) {
                try {
                    data = documentQueue.take();
                } catch (InterruptedException e) {
                    log.info(String.format("ES-Container got interrupted while waiting for a new entry. Queue size is : %d .", documentQueue.size()));
                    log.error(e.getMessage(), e);
                }
            }
            process(data);
        }
        log.info("Destroying ESContainer...");
    }


    public void add(T document) {
        while (document != null) {
            try {
                documentQueue.put(document);
                document = null;
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void add(List<T> document) {
        for (T o : document) {
            add(o);
        }
    }

    @Override
    public <R extends AbstractBaseDataElement> R create(T data) {
        Object response = executeThenGet(data);

        if (response instanceof IndexResponse) {
            data.data().documentId(((IndexResponse) response).getId());
        }

        return (R) data.data();
    }

    @Override
    public <R extends AbstractBaseDataElement> R createSynchronous(T data) {
        Object response = executeSynchronousRequest(data);

        if (response instanceof IndexResponse) {
            data.data().documentId(((IndexResponse) response).getId());
        }

        return (R) data.data();
    }


    public <TDATAELEMENT extends AbstractBaseDataElement, TE extends ESExtendedDataElement<TDATAELEMENT>> void search(ESSimpleResquestElement<TDATAELEMENT, TE> e) {
        try {
            GetRequest gq = new GetRequest(e.index(), e.id());

            if (e.routing() != null)
                gq.routing(e.routing());

            GetResponse r = client.get(gq, RequestOptions.DEFAULT);
            GsonBuilder gsonBuilder = new GsonBuilder();

            if (e.exclusionStrategies() != null && !e.exclusionStrategies().isEmpty())
                e.exclusionStrategies().forEach(gsonBuilder::addDeserializationExclusionStrategy);

            Gson gson = gsonBuilder.create();

            TDATAELEMENT d = gson.fromJson(r.getSourceAsString(), e.dataType());

            d.documentId(r.getId());

            e.response(d);
        } catch (JsonSyntaxException | IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public <R extends DocWriteResponse> R executeSynchronousRequest(T data) {
        try {
            lock.lock();
            if (data.operation() == ESOperationType.DELETE) {
                DeleteRequest dq = new DeleteRequest(data.index());
                dq.id(data.id());
                dq.routing(data.routing());
                dq.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
                return (R) client.delete(dq, RequestOptions.DEFAULT);
            } else if (data.operation() == ESOperationType.UPDATE) {
                UpdateRequest request = new UpdateRequest(data.index(), data.id());
                request.routing(data.routing());
                request.doc(new Gson().toJson(data.data()), XContentType.JSON);
                request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
                return (R) client.update(request, RequestOptions.DEFAULT);
            } else {
                IndexRequest request = new IndexRequest(data.index());
                request.routing(data.routing());
                request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
                request.source(new Gson().toJson(data.data()), XContentType.JSON);
                request.id(data.id());
                return (R) client.index(request, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public <R extends DocWriteResponse> R executeThenGet(T data) {
        try {
            if (data.operation() == ESOperationType.DELETE) {
                DeleteRequest dq = new DeleteRequest(data.index());
                dq.id(data.id());
                dq.routing(data.routing());
                return (R) client.delete(dq, RequestOptions.DEFAULT);
            } else if (data.operation() == ESOperationType.UPDATE) {
                UpdateRequest request = new UpdateRequest(data.index(), data.id());
                request.doc(new Gson().toJson(data.data()), XContentType.JSON);
                request.routing(data.routing());
                return (R) client.update(request, RequestOptions.DEFAULT);
            } else {
                IndexRequest request = new IndexRequest(data.index());
                request.routing(data.routing());
                request.source(new Gson().toJson(data.data()), XContentType.JSON);
                request.id(data.id());
                return (R) client.index(request, RequestOptions.DEFAULT);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    public void process(T data) {
        try {
            lock.lock();
            if (data.operation() == ESOperationType.DELETE) {
                DeleteRequest dq = new DeleteRequest(data.index());
                dq.id(data.id());
                dq.routing(data.routing());
                bulkRequest.add(dq);
            } else if (data.operation() == ESOperationType.UPDATE) {
                UpdateRequest request = new UpdateRequest(data.index(), data.id());
                request.doc(new Gson().toJson(data.data()), XContentType.JSON);
                request.routing(data.routing());
                bulkRequest.add(request);
            } else {
                IndexRequest request = new IndexRequest(data.index());
                request.routing(data.routing());
                request.source(new Gson().toJson(data.data()), XContentType.JSON);
                request.id(data.id());
                bulkRequest.add(request);
            }

            bulkCounter++;

            if (bulkCounter > bulkSize)
                flush();

        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        try {
            lock.lock();
            if (bulkCounter > 0) {
                client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(BulkResponse response) {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });

                bulkCounter = 0;

                try {
                    Thread.sleep(bulkWaitTime);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }

                preapreRequest();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isThereFreePlaceInTheQueue() {
        return queueSize() < bulkSize;
    }

    public int queueSize() {
        return documentQueue.size();
    }

    public int bulkSize() {
        return bulkCounter;
    }

    public void terminate() {
        try {
            lock.lock();
            isRunning = false;
            documentQueue.clear();
        } finally {
            lock.unlock();
        }
    }

}