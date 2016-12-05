import java.util.*;
import java.util.Map.Entry;

/**
 * Database class contains transactions that are not committed
 */
public class Db {
	private Stack<TransactionManager> stacks;

	/**
	 *  Default Constructor initializing the stack and adding an initial transaction
	 */
	public Db(){
		stacks = new Stack<>();
		stacks.push(new TransactionManager());
	}

	/**
	 * Set the transcaction name and value.
	 * @param name
	 * @param value
	 */
	public void set(String name, Integer value){
		stacks.peek().set(name, value);
	}

	/**
	 * Get the value of the latest transaction, if doesn't exists return NULL
	 * @param name
	 * @return value or NULL
	 */
	public Integer get(String name){
		return stacks.peek().get(name);
	}

	/**
	 * Get the number of counts that are currently set to value. If no variable are set return 0.
	 * @param value
	 * @return count or 0
	 */
	public Integer getCount(Integer value){
		return stacks.peek().getCount(value);
	}

	/**
	 * Apply and close all transactions, return false if there is no transaction
	 * @return boolean
	 */
	public boolean commit() {
		// if there is no transaction return false
		if (stacks.size() <= 1) return false;
		
		Map<String, Integer> values = new HashMap<>();
		Map<Integer, Integer> counts = new HashMap<>();

		Iterator<TransactionManager> it = stacks.iterator();
		while (it.hasNext()) {
			TransactionManager transaction = it.next();
			values.putAll(transaction.getValue());
		}
		
		for (Entry<String, Integer> entry : values.entrySet()) {
			Integer value = entry.getValue();
			if(counts.get(value) == null){
				counts.put(value, new Integer(1));
			}
			else{
				counts.put(value, new Integer(counts.get(value) + 1));
			}
			values.put(entry.getKey(),entry.getValue());
		}

		stacks = new Stack<>();
		stacks.push(new TransactionManager(values, counts));

		return true;
	}

	/**
	 * ROLLBACK : returns database to to previous transactional block, undo commands and remove the previous transaction
	 * if there is no transaction return false
	 * @return boolean
	 */
	public boolean rollBack(){
		if (stacks.size() <= 1) return false;
		stacks.pop();
		return true;
	}

	/**
	 * BEGIN : creates a new transactional block which can be rolledback
	 */
	public void begin(){
		TransactionManager block = new TransactionManager();
		block.setPrevious(stacks.peek());
		stacks.push(block);
	}
}
