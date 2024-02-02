package com.joshua.inventoryservice.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageModel {

    private String message;
    private boolean status;
    private Object data;
}
