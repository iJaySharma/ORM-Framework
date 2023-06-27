package dto;import com.thinking.machines.orm.annotation.*;
@Table(name="hibernate_sequence")
public class Hibernatesequence
{
@Column(name="next_val")
private Long nextVal;
public Hibernatesequence()
{
}
public void  setNextVal(Long nextVal)
{
this.nextVal=nextVal;
}
public Long getNextVal()
{
return this.nextVal;
}
}