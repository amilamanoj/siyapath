package org.siyapath.utils;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siyapath.NodeInfo;
import org.siyapath.service.NodeData;

/**
 * Created with IntelliJ IDEA.
 * User: Amila Manoj
 * Date: 8/29/12
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtilsTest extends TestCase {
    private static final Log log = LogFactory.getLog(CommonUtilsTest.class);


    public void testSerialize() throws Exception {
        log.info("Starting test: CommonUtils.serialize");
        NodeInfo input = new NodeInfo(8425,"127.0.0.1", 9000);
        NodeData output = CommonUtils.serialize(input);
        NodeData expectedOutput = new NodeData(8425,"127.0.0.1", 9000);
        assertEquals(output, expectedOutput);
        log.info("Serialize test is successful");
    }

    public void testDeSerialize() throws Exception {
        log.info("Starting test: CommonUtils.deSerialize");
        NodeData input = new NodeData(8425,"127.0.0.1", 9000);
        NodeInfo expectedOutput = new NodeInfo(8425,"127.0.0.1", 9000);
        NodeInfo output = CommonUtils.deSerialize(input);
        assertEquals(output, expectedOutput);
        log.info("Deserialize test is successful");
    }
}
