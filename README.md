ToDo Application!

This is a simple application which uses Jersey RESTful APIs integrated with Spring, storing data in MongoDB and deployed on Tomcat 7.

This app responds to cURL and browser requests. You can perform simple GET, PUT requests to search, get, delete, update and save. Upon an update where done is marked as true, an SMS is sent to notify user.

The JSON object representation is

{ 
  "title": "finish README article",
  "body": "some body",
  "done": false
}

The title is treated as an ID, and the user is not allowed to enter the same task title more than once. This is going by the assumption that you would not have the same ToDo tasks listed more than once.

To build and use:
Please use Maven to generate a war file and deploy it on a Tomcat Server.

Some example cURL commands that you can try on this app are:
Create new
curl http://localhost:8080/ToDoApp/todo/ -H "Content-type:application/json" -X POST -d {"title":"daftpunk","body":"get lucky!","done":false}
Retrieve one
curl http://localhost:8080/ToDoApp/todo/daftpunk -H "Content-type:application/json" -X GET
Update
curl http://localhost:8080/ToDoApp/todo/daftpunk -H "Content-type:application/json" -X PUT -d {"done":"true"}
Delete
curl http://localhost:8080/ToDoApp/todo/daftpunk -H "Content-type:application/json" -X DELETE