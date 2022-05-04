# expert-lamp

## high level overview
This proxy provides a cache in front of a redis server. If it isn't in the cache, it looks for it in redis.
If it isn't in redis; it returns a 404 NOT_FOUND.

cache is in the format of INT:STRING; INT being the key, the string being the value. It uses an data class called "InterestingObject"; but we only care about the 'value' member. We could store the whole object, and return it even.

This project uses
* spring-boot to provide most of the boilerplate backend
* lettuce as the redis client (https://lettuce.io/)
* google's guava cache (https://github.com/google/guava)

## code breakdown
RedisConfiguration -> Sets up the beans for the redis client.
Controller -> HTTP GET endpoint
GlobalExceptionHandler -> Handles exceptions, providing error conditions to the client
RedisService -> Front end for the redis client to read/write to redis.
LocalCache -> Contains the guava cache, with logic to call into redisservice if the cache does not contain what is asked for
Main -> Startup code

## algorithmic complexity
google's guava cache uses a ConcurrentHashMap under the hood, and in JDK11 the operations we are using (load,save) and the implied LRU delete should be near O(1) time.
redis claims near O(1) time as well, and for the tested cache size (10) it shouldn't be a challenge.

## how to run
ensure redis is running on an accessible host:port (ie docker-compose w/ localhost:6379)
populate redis with data
update resources/application.yml with correct host/port for redis
./gradlew bootRun
curl `localhost:8080/v1/get/{key}` where key is an int 

## time taken
* redis library: 4 hours
* cache: 30min
* testing: 6 hours (getting embedded redis to work was a challenge)

## not implemented
Upper bound testing; one presumes this project will fall apart if the cache allocated was overly massive, or at least would slow down.
