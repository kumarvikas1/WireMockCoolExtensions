WireMockCool Extensions allows condition, javascript and java pojos to manipulate the body of the response(XML/JSON).

It obviously written om top of wiremock http://wiremock.org/

##Installation
```
<dependency>
    <groupId>com.github.kumarvikas1</groupId>
    <artifactId>wiremock_cool_extensions</artifactId>
    <version>1.0.0</version>
</dependency>
```

##How It Works

###Extend Response Generator
Response Generator needs to overriden if we want to update Java Code and applies method return boolean to return applicable response generator

```
@Component
public class Test1Generator extends AbstractResponseGenerator {

@Override
    public boolean applies(Request request) {
        return request.getAbsoluteUrl().contains("test1");
    }
  
```

Implement the getResponse method to return the updated Body.

```
 @Override
    public ResponseDefinition getResponse() {
```

###Update Body in .json files

####Key
```
"body": "Country is key#key Country#key And currency is key#key Currency#key",
```
Get request 
```
test3/?Country=America&Currency=USD
```

will result in
```
Country is America And currency is USD
```

###If Else

```
"body": "Currency is if#given Country equals America then USD else NA#if",
```
Get request 
```
test3/?Country=America
```

will result in
```
Currency is USD
```

###Show If

Body
```
 {
  "data": [
    {
      "id": "X999_Y999",
      "from": {
        "name": if#given Country equals America then USD else NA#if, "id": "X12"
      },
      "message": "Looking forward to 2010!",
      "actions": [
        {
          "name": "Country is key#key Country#key",
          "link": "http://www.facebook.com/X999/posts/Y999"
        },
        show#given Country equals America then show

          "name": "Like and if#given Country equals America then USD else NA#if And Country is key#key Country#key",
          "link": "http://www.facebook.com/X999/posts/Y999"
        }
        #show
      ],
        show#given Country equals America then show
      "type": "status",
      "created_time": "2010-08-02T21:27:44+0000",
      "updated_time": "2010-08-02T21:27:44+0000"
        #show
    },
```
Get request 
```
test3/?Country=America
```

will result in
```
{
  "data": [
    {
      "id": "X999_Y999",
      "from": {
        "name": USD, "id": "X12"
      },
      "message": "Looking forward to 2010!",
      "actions": [
        {
          "name": "Country is America",
          "link": "http://www.facebook.com/X999/posts/Y999"
        },
        

          "name": "Like and USD And Country is America",
          "link": "http://www.facebook.com/X999/posts/Y999"
        }
        
      ],
        
      "type": "status",
      "created_time": "2010-08-02T21:27:44+0000",
      "updated_time": "2010-08-02T21:27:44+0000"
        
    },
```

And Get request 
```
test3/?Country=India
```
will result in
```
{
  "data": [
    {
      "id": "X999_Y999",
      "from": {
        "name": NA, "id": "X12"
      },
      "message": "Looking forward to 2010!",
      "actions": [
        {
          "name": "Country is India",
          "link": "http://www.facebook.com/X999/posts/Y999"
        },
        
      ],
        
    },
```

###Java Script

Body
```
"body": "Country is with Currency #scriptload(\"src/main/resources/mappingsResponse/js/foo.js\");#script"
```

Script
```
var value = function (object) {
    var MyJavaClass = Java.type('com.stubs.cool_extensions.glue.JavaScriptHelper');
    return MyJavaClass.getJSONValue(object, 'Currency');
}
```

Get request 
```
test3/?Currency=USD
```

will result in
```
Country is with Currency USD
```

###Create globalMappings.json to simply make a regular expression replaced in the body with the request body value

```
{
  "global_mappings": [
    {
      "url": "(.*)test3/?Currency=USD",
      "match": "\"Currency value is (.*)\"",
      "path": "Currency"
    },
```
