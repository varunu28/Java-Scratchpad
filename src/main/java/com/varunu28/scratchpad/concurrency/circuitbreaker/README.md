# Circuit breaker pattern

The request is done through the method exposed by the `CircuitBreaker` interface. The underlying implementation invokes
the method only if the circuit is closed. If the circuit is open, it checks if the cool off period has elapsed. If it
has then it resets the circuit to closed and invokes the method. If the cool off period has not elapsed, it returns
false. 