package syw.mq.v2.enity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@AllArgsConstructor
@Data
public class Message<T> {

    private HashMap<String,Object> headers;

    private T body;

}
