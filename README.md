WireMockCool Extensions allows condition, javascript and java pojos to manipulate the body of the response(XML/JSON).

It obviously written om top of wiremock http://wiremock.org/

##How It Works

###Extend Response Generator
Response Generator needs to overriden if we want to update Java Code and applies method return boolean to return applicable response generator

```
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

Update the defaults.conf with the name of class extending Abstract Response Generator
```
response = [
  "com.hotels.rtt.generators.Test1Generator",
  "com.hotels.rtt.generators.Test2Generator",
  "com.hotels.rtt.generators.Test5Generator"
]
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
ountry is America And currency is USD
```



