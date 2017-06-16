An example Spring Boot project using HRRS.

Project employs the following components of the `hrrs-servlet-filter-base64`
artifact:

- `Base64HrrsFilter` is used to inject a servlet filter to record incoming
  HTTP requests. The recorded requests are written to a file created at
  start-up via `java.io.File.createTempFile("hrrs-spring-records-", ".csv")`.

- `HrrsServlet` is mapped to `/hrrs` endpoint to enable/disable the servlet
  filter dynamically.

You can start the application either through `exec:java` Maven goal or by
running the `HelloApplication#main()` method in your IDE. The provided
`HelloController` exposes a `POST` method handler on `/hello?name=<name>`
endpoint which you can query.

```
$ git clone git@github.com:vy/hrrs.git          # clone the project
$ cd hrrs
$ mvn install                                   # make sure HRRS artifacts are placed into your local repository
$ mvn -pl examples/spring exec:java             # run the exec:java goal
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building hrrs-example-spring 0.2-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ hrrs-example-spring ---
2017-05-05 11:14:22.225  INFO 7590 --- [lication.main()] c.v.h.example.spring.HelloApplication    : Starting HelloApplication on varlik with PID 7590 (/home/vy/Projects/bol/hrrs/examples/spring/target/classes started by vy in /home/vy/Projects/bol/hrrs)
2017-05-05 11:14:22.230 DEBUG 7590 --- [lication.main()] c.v.h.example.spring.HelloApplication    : Running with Spring Boot v1.4.2.RELEASE, Spring v4.3.4.RELEASE
2017-05-05 11:14:22.230  INFO 7590 --- [lication.main()] c.v.h.example.spring.HelloApplication    : No active profile set, falling back to default profiles: default
2017-05-05 11:14:22.317  INFO 7590 --- [lication.main()] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@30b643cf: startup date [Fri May 05 11:14:22 CEST 2017]; root of context hierarchy
2017-05-05 11:14:23.281  INFO 7590 --- [lication.main()] f.a.AutowiredAnnotationBeanPostProcessor : JSR-330 'javax.inject.Inject' annotation found and supported for autowiring
2017-05-05 11:14:23.765  INFO 7590 --- [lication.main()] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 8080 (http)
2017-05-05 11:14:23.779  INFO 7590 --- [lication.main()] o.apache.catalina.core.StandardService   : Starting service Tomcat
2017-05-05 11:14:23.780  INFO 7590 --- [lication.main()] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.6
2017-05-05 11:14:23.906  INFO 7590 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2017-05-05 11:14:23.907  INFO 7590 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1593 ms
2017-05-05 11:14:24.042 TRACE 7590 --- [ost-startStop-1] .h.s.f.HttpRequestRecordWriterFileTarget : instantiated (file=/tmp/hrrs-spring-records-1919023531293728658.csv, charset=US-ASCII)
2017-05-05 11:14:24.108  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'hrrsServlet' to [/hrrs]
2017-05-05 11:14:24.110  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'dispatcherServlet' to [/]
2017-05-05 11:14:24.114  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
2017-05-05 11:14:24.114  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2017-05-05 11:14:24.115  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
2017-05-05 11:14:24.115  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
2017-05-05 11:14:24.115  INFO 7590 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'provideHrrsFilter' to: [/*]
2017-05-05 11:14:24.145 TRACE 7590 --- [ost-startStop-1] com.vlkan.hrrs.servlet.HrrsFilter        : initialized
2017-05-05 11:14:24.464  INFO 7590 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@30b643cf: startup date [Fri May 05 11:14:22 CEST 2017]; root of context hierarchy
2017-05-05 11:14:24.560  INFO 7590 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/hello],methods=[POST],consumes=[text/plain],produces=[text/plain]}" onto public org.springframework.http.ResponseEntity<java.lang.String> com.vlkan.hrrs.example.spring.HelloController.sayHello(java.lang.String,byte[])
2017-05-05 11:14:24.565  INFO 7590 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],produces=[text/html]}" onto public org.springframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
2017-05-05 11:14:24.566  INFO 7590 --- [lication.main()] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error]}" onto public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
2017-05-05 11:14:24.614  INFO 7590 --- [lication.main()] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-05-05 11:14:24.614  INFO 7590 --- [lication.main()] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-05-05 11:14:24.664  INFO 7590 --- [lication.main()] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-05-05 11:14:24.804  INFO 7590 --- [lication.main()] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2017-05-05 11:14:24.859  INFO 7590 --- [lication.main()] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2017-05-05 11:14:24.864  INFO 7590 --- [lication.main()] c.v.h.example.spring.HelloApplication    : Started HelloApplication in 3.353 seconds (JVM running for 5.843)
```

See the `/tmp/hrrs-spring-records-1919023531293728658.csv` file in the output?
That's the file where the recorded HTTP requests will be stored. Let's start
with querying the state of the servlet filter:

```
$ curl http://localhost:8080/hrrs
{"enabled": true}
```

We are good to go. Now we can create some noise.

```
$ curl -H "Content-Type: text/plain" -d "some random payload" http://localhost:8080/hello?name=Volkan
Hello, Volkan! (19)
```

Note the `Hello, Volkan! (19)` reply we got from the endpoint. Let's create
some more noise to force JVM to flush the file buffers.

```
$ for iter in $(seq 100); do
  curl -H "Content-Type: text/plain" -d "payload-$iter" "http://localhost:8080/hello?name=User-$iter";
  done
Hello, User-1! (22)
Hello, User-2! (22)
...
Hello, User-98! (24)
Hello, User-99! (24)
Hello, User-100! (26)
```

Now we can examine the recorded HTTP requests:

```bash
$ head -n 1 /tmp/hrrs-spring-records-1919023531293728658.csv
j2bntok7_1ncux  20170505-115026.455+0200  hello  POST  ABIvaGVsbG8/bmFtZT1Wb2xrYW4AAAAFAARob3N0AA5sb2NhbGhvc3Q6ODA4MAAKdXNlci1hZ2VudAALY3VybC83LjQ3LjAABmFjY2VwdAADKi8qAA5jb250ZW50LWxlbmd0aAACMTkADGNvbnRlbnQtdHlwZQAhYXBwbGljYXRpb24veC13d3ctZm9ybS11cmxlbmNvZGVkAAAAAAAAAAAAAAAA

$ head -n 3 /tmp/hrrs-spring-records-9056751296977530502.csv | awk '{print $5}' | base64 --decode | hd
00000000  00 12 2f 68 65 6c 6c 6f  3f 6e 61 6d 65 3d 56 6f  |../hello?name=Vo|
00000010  6c 6b 61 6e 00 00 00 05  00 04 68 6f 73 74 00 0e  |lkan......host..|
00000020  6c 6f 63 61 6c 68 6f 73  74 3a 38 30 38 30 00 0a  |localhost:8080..|
00000030  75 73 65 72 2d 61 67 65  6e 74 00 0b 63 75 72 6c  |user-agent..curl|
00000040  2f 37 2e 34 37 2e 30 00  06 61 63 63 65 70 74 00  |/7.47.0..accept.|
00000050  03 2a 2f 2a 00 0e 63 6f  6e 74 65 6e 74 2d 6c 65  |.*/*..content-le|
00000060  6e 67 74 68 00 02 31 39  00 0c 63 6f 6e 74 65 6e  |ngth..19..conten|
00000070  74 2d 74 79 70 65 00 21  61 70 70 6c 69 63 61 74  |t-type.!applicat|
00000080  69 6f 6e 2f 78 2d 77 77  77 2d 66 6f 72 6d 2d 75  |ion/x-www-form-u|
00000090  72 6c 65 6e 63 6f 64 65  64 00 00 00 00 00 00 00  |rlencoded.......|
000000a0  00 00 00 00 00 00 12 2f  68 65 6c 6c 6f 3f 6e 61  |......./hello?na|
000000b0  6d 65 3d 55 73 65 72 2d  31 00 00 00 05 00 04 68  |me=User-1......h|
000000c0  6f 73 74 00 0e 6c 6f 63  61 6c 68 6f 73 74 3a 38  |ost..localhost:8|
000000d0  30 38 30 00 0a 75 73 65  72 2d 61 67 65 6e 74 00  |080..user-agent.|
000000e0  0b 63 75 72 6c 2f 37 2e  34 37 2e 30 00 06 61 63  |.curl/7.47.0..ac|
000000f0  63 65 70 74 00 03 2a 2f  2a 00 0e 63 6f 6e 74 65  |cept..*/*..conte|
00000100  6e 74 2d 6c 65 6e 67 74  68 00 01 39 00 0c 63 6f  |nt-length..9..co|
00000110  6e 74 65 6e 74 2d 74 79  70 65 00 21 61 70 70 6c  |ntent-type.!appl|
00000120  69 63 61 74 69 6f 6e 2f  78 2d 77 77 77 2d 66 6f  |ication/x-www-fo|
00000130  72 6d 2d 75 72 6c 65 6e  63 6f 64 65 64 00 00 00  |rm-urlencoded...|
00000140  00 00 00 00 00 00 00 00  00 00 12 2f 68 65 6c 6c  |.........../hell|
00000150  6f 3f 6e 61 6d 65 3d 55  73 65 72 2d 32 00 00 00  |o?name=User-2...|
00000160  05 00 04 68 6f 73 74 00  0e 6c 6f 63 61 6c 68 6f  |...host..localho|
00000170  73 74 3a 38 30 38 30 00  0a 75 73 65 72 2d 61 67  |st:8080..user-ag|
00000180  65 6e 74 00 0b 63 75 72  6c 2f 37 2e 34 37 2e 30  |ent..curl/7.47.0|
00000190  00 06 61 63 63 65 70 74  00 03 2a 2f 2a 00 0e 63  |..accept..*/*..c|
000001a0  6f 6e 74 65 6e 74 2d 6c  65 6e 67 74 68 00 01 39  |ontent-length..9|
000001b0  00 0c 63 6f 6e 74 65 6e  74 2d 74 79 70 65 00 21  |..content-type.!|
000001c0  61 70 70 6c 69 63 61 74  69 6f 6e 2f 78 2d 77 77  |application/x-ww|
000001d0  77 2d 66 6f 72 6d 2d 75  72 6c 65 6e 63 6f 64 65  |w-form-urlencode|
000001e0  64 00 00 00 00 00 00 00  00 00 00 00 00           |d............|
000001ed
```

So far, so good. Before being able to replay recorded requests, we need
to remove certain headers such as `Host` and `Content-Length`. We can
use Distiller for this purpose. We first need to create a transformation
script.

```javascript
/**
 * Distiller transformation script.
 * Removes `Host` and `Content-Length` headers.
 */

function sanitizeHeaders(oldHeaders) {
    var newHeaders = [];
    for (var oldHeaderIndex = 0; oldHeaderIndex < oldHeaders.length; oldHeaderIndex++) {
        var oldHeader = oldHeaders[oldHeaderIndex];
        var oldHeaderName = oldHeader.getName();
        var allowed =
            !oldHeaderName.equalsIgnoreCase("host") &&
            !oldHeaderName.equalsIgnoreCase("content-length");
        if (allowed) {
            newHeaders.push(oldHeader);
        }
    }
    return newHeaders;
}

function transform(input) {
    var newHeaders = sanitizeHeaders(input.getHeaders());
    return input.toBuilder().setHeaders(newHeaders).build();
}

```

Distiller will walk over each recorded request, call `transform(input)`, and
persist the emitted output, except when it is null. Distiller uses the default
JavaScript engine shipped with Java, which happens to be
[Rhino](https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino) for
J2SE 6. The provided `input` parameter within the `transform` function is of
type `com.vlkan.hrrs.api.HttpRequestRecord`. Hence, you can leverage any
method and field available in the Java class.

After saving the above transformation script to `/tmp/transform.js`, you can
use the distiller as follows:

```
$ java \
  -jar /path/to/hrrs-distiller-base64-<version>.jar \
  -i file:/tmp/hrrs-spring-records-1919023531293728658.csv \
  -o file:/tmp/hrrs-spring-records-1919023531293728658-distilled.csv \
  -s file:/tmp/transform.js
2017-06-16 22:27:18 [ INFO] [main] com.vlkan.hrrs.distiller.cli.Distiller.run:73 - totalRecordCount = 101
2017-06-16 22:27:18 [ INFO] [main] com.vlkan.hrrs.distiller.cli.Distiller.run:75 - ignoredRecordCount = 0 (0.0%)
2017-06-16 22:27:18 [ INFO] [main] com.vlkan.hrrs.distiller.cli.Distiller.run:77 - changedRecordCount = 101 (100.0%)
```

Let's verify the contents of the distilled records.

```
$ tail -n 1 /tmp/hrrs-spring-records-1919023531293728658-distilled.csv | awk '{print $5}' | base64 --decode | strings
/hello?name=DistilledUser-100
user-agent
curl/7.47.0
accept
content-type
text/plain
payload-100
```

Now we can replay the distilled records against the same Spring application
we started:

```
$ java \
  -jar /path/to/hrrs-replayer-base64-<version>.jar \
  -th localhost \
  -tp 8080 \
  -i file:/tmp/hrrs-spring-records-4386205311507695498-distilled.csv
100.0% (durationMillis=10513, recordCount=11)

6/16/17 11:32:49 PM ============================================================

-- Timers ----------------------------------------------------------------------
__all__
             count = 11
         mean rate = 1.06 calls/second
     1-minute rate = 1.00 calls/second
     5-minute rate = 1.00 calls/second
    15-minute rate = 1.00 calls/second
               min = 3.00 milliseconds
               max = 167.00 milliseconds
              mean = 18.99 milliseconds
            stddev = 44.95 milliseconds
            median = 4.00 milliseconds
              75% <= 9.00 milliseconds
              95% <= 167.00 milliseconds
              98% <= 167.00 milliseconds
              99% <= 167.00 milliseconds
            99.9% <= 167.00 milliseconds
__all__.200
             count = 11
         mean rate = 1.07 calls/second
     1-minute rate = 1.00 calls/second
     5-minute rate = 1.00 calls/second
    15-minute rate = 1.00 calls/second
               min = 3.00 milliseconds
               max = 167.00 milliseconds
              mean = 18.99 milliseconds
            stddev = 44.95 milliseconds
            median = 4.00 milliseconds
              75% <= 9.00 milliseconds
              95% <= 167.00 milliseconds
              98% <= 167.00 milliseconds
              99% <= 167.00 milliseconds
            99.9% <= 167.00 milliseconds
hello
             count = 11
         mean rate = 1.07 calls/second
     1-minute rate = 1.00 calls/second
     5-minute rate = 1.00 calls/second
    15-minute rate = 1.00 calls/second
               min = 3.00 milliseconds
               max = 167.00 milliseconds
              mean = 18.99 milliseconds
            stddev = 44.95 milliseconds
            median = 4.00 milliseconds
              75% <= 9.00 milliseconds
              95% <= 167.00 milliseconds
              98% <= 167.00 milliseconds
              99% <= 167.00 milliseconds
            99.9% <= 167.00 milliseconds
hello.200
             count = 11
         mean rate = 1.07 calls/second
     1-minute rate = 1.00 calls/second
     5-minute rate = 1.00 calls/second
    15-minute rate = 1.00 calls/second
               min = 3.00 milliseconds
               max = 167.00 milliseconds
              mean = 18.99 milliseconds
            stddev = 44.95 milliseconds
            median = 4.00 milliseconds
              75% <= 9.00 milliseconds
              95% <= 167.00 milliseconds
              98% <= 167.00 milliseconds
              99% <= 167.00 milliseconds
            99.9% <= 167.00 milliseconds
```

Replayer is a very versatile command line tool. Pass `--help` or `-h` to list
all its available options.
