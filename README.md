WireMockCool Extensions allows condition, javascript and java pojos to manipulate the body of the response.

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



