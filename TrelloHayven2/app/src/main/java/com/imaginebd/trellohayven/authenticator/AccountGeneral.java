package com.imaginebd.trellohayven.authenticator;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 20/03/13
 * Time: 18:11
 */
public class AccountGeneral {

    /**
     * Account type id, needs to be the same as accountType in authenticator.xml
     */
    public static final String ACCOUNT_TYPE = "com.imaginebd.trello_sync";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "Hayven - Trello";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Trello account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Trello account";
    
    public static final String API_KEY = "ee531a6418b5706b6a4d8ed0452dd396";
    public static final String API_SECRET = "11a745b9d4556941231e06c6eb76d52d908b4de932e9ad94e9f0d2ce9eab9a24";

}
