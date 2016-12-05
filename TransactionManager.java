import java.util.HashMap;
import java.util.Map;

/**
 * A transaction class
 */
class TransactionManager {
    // Points to the previous transaction
    private TransactionManager previous;
    private Map<String, Integer> values;
    private Map<Integer, Integer> counts;

    /**
     * Default Constructor;
     */
    public TransactionManager(){
        values = new HashMap<>();
        counts = new HashMap<>();
    }

    /**
     * Alternative Constructor
     * @param val
     * @param counts
     */
    public TransactionManager(Map<String, Integer> val, Map<Integer, Integer> counts){
        this.values = val;
        this.counts = counts;
    }

    /**
     * Set previous transaction
     * @param transaction
     */
    public void setPrevious(TransactionManager transaction) {
        previous = transaction;
    }

    /**
     * Getter for value
     * @return values
     */
    public Map<String, Integer> getValue(){
        return values;
    }

    /**
     * SET [variable_name] [value] : stores [variable_name] with [value]
     * UNSET [variable_name] : deletes [variable_name]
     * @param key
     * @param currVal
     */
    public void set(String key, Integer currVal){

        // get the previous count, if not null decrease the count val by 1
        Integer prevVal = get(key);
        if (prevVal != null){
            Integer preValCount = getCount(prevVal);
            preValCount -= 1;
            counts.put(prevVal, preValCount);
        }

        // get the curr val count, if exists decrease by 1
        Integer currValCount = getCount(currVal);
        if (currVal != null) {
            if (currValCount != null) {
                currValCount += 1;
                counts.put(currVal, currValCount);
            } else {
                counts.put(currVal, new Integer(1));
            }
        }

        values.put(key, currVal);
    }

    /**
     * GET [variable_name] : returns value associated with [variable_name]
     * @param name
     * @return value
     */
    public Integer get(String name) {
        TransactionManager block = this;
        Integer value = block.values.get(name);
        while(!block.values.containsKey(name) && block.previous != null){
            block = block.previous;
            value = block.values.get(name);
        }
        return value;
    }

    /**
     * NUMEQUALTO [value] : returns number of variables with same [value]
     * @param value
     * @return the number of variables with the same value or 0 if not variable equal to the value
     */
    public Integer getCount(Integer value){
        if (value == null) return 0;

        TransactionManager block = this;
        Integer counter = block.counts.get(value);
        while(counter == null && block.previous != null){
            block = block.previous;
            counter = block.counts.get(value);
        }

        if (counter == null)
            return 0;
        else{
            return counter;
        }
    }
}
