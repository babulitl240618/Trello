package com.imaginebd.trellohayven3.trello.parsers;

import java.io.InputStream;

public interface Parser<T> {
    T parse(InputStream inputStream);
}
