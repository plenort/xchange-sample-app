# xchange-sample-app
Sample app for monitoring prices with Xchange library, uses H2 database with spring-boot.

Run with:
mvn spring-boot:run

Access:
http://localhost:8080/

Test with:
mvn test

Manual UI Test steps:
1. Open http://localhost:8080/
2. Add Currency Pair: "BTC/USD", Limit: 1 - press Add
3. Connect websocket - press Connect
4. Alerts will be distributed over stomp websocket channel "/topic/alerts", and should appear on the frontend every 10 seconds
(time interval configurable in applicaiton.yml -> interval.in.milliseconds)
5. Click on any alert, on the stored alerts list
6. Click remove button
