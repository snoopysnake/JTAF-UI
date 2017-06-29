package Tests;

import JTAF.Parameter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michael on 06/28/2017.
 */
public class ParameterTest {
    @Test
    public void paramTest() {
        Parameter param = new Parameter("a","b");
        Assert.assertEquals(param.getName(),"a");
        Assert.assertEquals(param.getTag(),"b");
        Assert.assertEquals(null,param.getText());
    }
}