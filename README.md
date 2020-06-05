# sunbird-apimanager-util
Wrapper for Kong admin util

This wrapper exposes APIs which can be used to register kong consumers and credentials. This service should not be exposed to the internet.


## Steps to run this code
1. Create 1 or more key pairs using the following steps
```
    //device is the prefix and 0 is the key number. The meaning of this would be clear while updating the stack.yml
    openssl genrsa -des3 -out device0.pem 2048
    openssl rsa -in device0.pem -out device0.key
    openssl rsa -in device0.pem -outform PEM -pubout -out device0.pub  //if you want to retrieve the public key
    openssl pkcs8 -topk8 -inform PEM -in device0.pem -out device0 -nocrypt
```
2. Update the stack.yml with the following properties
```
am.admin.api.jwt.keycount - should be updated to the number of keys you generated. The code assumes that the keys are ordered from 0 - n. 
am.admin.api.jwt.keyprefix - In the key generation i am using the prefix of *device* for the keys.
Also update the secrets to reflect the path where you have created the keys
```
3. docker-compose -f stack.yml up
