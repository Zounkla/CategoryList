package org.example.tp1.examples;

public class JsonExample {
 public final static String CATEGORIES_EXAMPLE =
            "{\n" +
            "    \"categories\": {\n" +
            "        \"DC\": {\n" +
            "            \"children\": \"[Batman, Superman]\",\n" +
            "            \"date\": \"2024-12-04 09:19:48.176\",\n" +
            "            \"id\": \"1\",\n" +
            "            \"name\": \"DC\",\n" +
            "            \"parent\": \"null\"\n" +
            "        },\n" +
            "        \"Batman\": {\n" +
            "            \"children\": \"[]\",\n" +
            "            \"date\": \"2024-12-04 09:19:48.187\",\n" +
            "            \"id\": \"2\",\n" +
            "            \"name\": \"Batman\",\n" +
            "            \"parent\": \"DC\"\n" +
            "        },\n" +
            "        \"Superman\": {\n" +
            "            \"children\": \"[]\",\n" +
            "            \"date\": \"2024-12-04 09:19:48.187\",\n" +
            "            \"id\": \"3\",\n" +
            "            \"name\": \"Superman\",\n" +
            "            \"parent\": \"DC\"\n" +
            "        },\n" +
            "        \"pageCount\": \"2\"\n" +
            "    }\n" +
                    "}";

    public static final String CATEGORY_EXAMPLE =
            " {\n" +
                    "   \"date\":\"2024-12-04 09:19:48.176\",\n" +
                    "   \"name\":\"Batman\",\n" +
                    "   \"parent\":\"null\",\n" +
                    "   \"id\":\"3\",\n" +
                    "   \"children\":\"[zaaaaaaaaaa]\"\n" +
                    "}";

    public static final String CATEGORY_NOT_FOUND_EXAMPLE =
            "{\n" +
                    "   \"message\":\"Category does not exists\",\n" +
                    "   \"status\":\"404\"\n" +
                    "}";

    public static final String CATEGORY_ERROR_PRECOND_EXAMPLE  =
            "{\n" +
                    "   \"message\":\"<Error message>\",\n" +
                    "   \"status\":\"412\"\n" +
                    "}";

    public static final String INSERT_EXAMPLE =
            "{\n" +
                    "   \"name\":\"Batman\",\n" +
                    "   \"oldName\":\"none\",\n" +
                    "   \"parentName\":\"DC\"\n" +
                    "}";
}
