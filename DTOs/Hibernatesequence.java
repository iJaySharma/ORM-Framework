import com.thinking.machines.orm.annotations.*;
@Table(name="hibernate_sequence")
public class Hibernatesequence
{
@Column(name="next_val")
private  nextVal;
public Hibernatesequence()
{
}
public void  setNextVal( nextVal)
{
this.nextVal=nextVal;
}
public  getNextVal()
{
return this.nextVal;
}
}