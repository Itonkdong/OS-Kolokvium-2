package Networking.ExtendClasses;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Request
{

    private final String method;

    private final String uri;

    private final String httpVersion;

    private final Map<String, String> headers;

    private final Map<String, String> queryParameters;

    private final String body;

    public Request(String method, String uri, String httpVersion, Map<String, String> headers, String body, Map<String, String> queryParameters)
    {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
        this.queryParameters = queryParameters;
    }

    public static Request of(String requestString)
    {
        String[] parts = requestString.split("\\n\\n");

        String head = parts[0];
        String body = null;

        String[] headParts = head.split("\\n");

        String requestLine = headParts[0];
        String[] requestLineParts = requestLine.split("\\s+");
        String method = requestLineParts[0];
        String uri = requestLineParts[1];
        String httpVersion = requestLineParts[2];
        Map<String, String> queryParameters = parseQueryParameters(uri);
        LinkedHashMap<String, String> headers;
        if(headParts.length > 1)
        {
            headers = Arrays.stream(headParts).skip(1).collect(Collectors.toMap(
                    s -> s.split(":")[0],
                    s -> s.split(":")[1],
                    (o, n) -> n,
                    LinkedHashMap::new
            ));
        }
        else
        {
            headers = new LinkedHashMap<>();
        }



        if (parts.length > 1)
        {
            body = parts[1];
        }

        return new Request(method, uri, httpVersion, headers, body, queryParameters);
    }

    private static Map<String,String> parseQueryParameters(String uri)
    {
        String[] parts = uri.split("\\?");
        if(parts.length == 1)
        {
            return new LinkedHashMap<>();
        }

        String queryString = parts[1];
        String[] queryParts = queryString.split("&");
        LinkedHashMap<String, String> queryParameters = Arrays.stream(queryParts).collect(Collectors.toMap(
                s -> s.split("=")[0],
                s -> s.split("=")[1],
                (o, n) -> n,
                LinkedHashMap::new

        ));
        return queryParameters;

    }

    public String getMethod()
    {
        return method;
    }

    public String getUri()
    {
        return uri;
    }

    public String getHttpVersion()
    {
        return httpVersion;
    }

    public String getHeader(String headerKey)
    {
        return this.headers.get(headerKey);
    }

    public String getBody()
    {
        return body;
    }

    public String getQueryParameter(String key)
    {
        return this.queryParameters.get(key);
    }


    public void testPrint()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===REQUEST LINE===\n");
        stringBuilder.append(String.format("%s %s %s\n", method, uri, httpVersion));
        stringBuilder.append("\n===HEADERS===\n");
        this.headers.entrySet().stream().forEach(entry->stringBuilder.append(String.format("%s:%s\n", entry.getKey(), entry.getValue())));
        stringBuilder.append("\n===BODY===\n");
        stringBuilder.append(Objects.requireNonNullElse(this.body, "NONE"));
        stringBuilder.append("\n\n===QUERY PARAMETERS===\n");
        this.queryParameters.entrySet().stream().forEach(entry->stringBuilder.append(String.format("%s:%s\n", entry.getKey(), entry.getValue())));
        System.out.println(stringBuilder);
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%s %s %s\n", method, uri, httpVersion));
        this.headers.entrySet().stream().forEach(entry->stringBuilder.append(String.format("%s:%s\n", entry.getKey(), entry.getValue())));
        stringBuilder.append(Objects.requireNonNullElse(this.body, ""));
        return stringBuilder.toString();
    }

    public static void main(String[] args)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GET /?name=viktor HTTP/1.1\n");
        stringBuilder.append("User:Mozilla\n");
        stringBuilder.append("Content-type:text/*\n");
        stringBuilder.append("\n");
        stringBuilder.append("Hello world");
        Request request = Request.of(stringBuilder.toString());
        request.testPrint();
        System.out.println(request);
    }
}
