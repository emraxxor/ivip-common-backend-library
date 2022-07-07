package com.github.emraxxor.ivip.common.es;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ESDefaultJoinRelation<ID> {
    String name;
    ID parent;
}
