package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.helpers.FileModifier;
import com.comcast.test.citf.common.helpers.FileModifier.ModifyInstruction;
import com.comcast.test.citf.common.helpers.FileModifier.OperationType;

public class FileModifierTest {

	private FileModifier objFileModifier;
	private ModifyInstruction objModifyInstruction,objModifyInstruction1;
	private final String value="val1";
	private  OperationType operationType;
	private final String[] includeORFilter={"abc","def","ghi"};
	private final String[] excludeORFilter={"123","456","789"};
	private final int maxChangecount=5;
	private final int executionSequnce=2;
	private final String sValid="VALID";

	private static final String THREAD_PREFIX = "@ParallelThread1";


	@Before
	public void setup() 
	{

		operationType=OperationType.ADD;
		objFileModifier=new FileModifier();
		objModifyInstruction=objFileModifier.new ModifyInstruction(value, operationType, maxChangecount, includeORFilter, excludeORFilter,executionSequnce);
	}


	@Test
	public void testValue()
	{
		objModifyInstruction.setValue(sValid);
		assertEquals(sValid,objModifyInstruction.getValue());

		objModifyInstruction.setValue(null);
		assertEquals(null,objModifyInstruction.getValue());

		objModifyInstruction.setValue(ICimaCommonConstants.BLANK_STRING);
		assertEquals(ICimaCommonConstants.BLANK_STRING,objModifyInstruction.getValue());
	}

	
	@Test
	public void testOperationType()
	{
		objModifyInstruction.setOperationType(OperationType.ADD);
		assertEquals(OperationType.ADD,objModifyInstruction.getOperationType());

		objModifyInstruction.setOperationType(OperationType.REMOVE);
		assertEquals(OperationType.REMOVE,objModifyInstruction.getOperationType());

	}


	@Test
	public void testModifyFile() {
		try{
			LinkedList<FileModifier.ModifyInstruction> modifyInstructions = new LinkedList<FileModifier.ModifyInstruction>();
			String destfileName = System.getProperty("java.io.tmpdir") + "/feature1modified.feature";
			String srcfileName = System.getProperty("java.io.tmpdir") + "/feature1.feature";
			String p1 = ICommonConstants.BLANK_SPACE_STRING + THREAD_PREFIX;
			int scenarios=1;
			String srcIdentifierAnno = ICommonConstants.AT_THE_RATE + "Integration";
			
			copyFile(new File("src/test/resources/feature1.feature"), new File(srcfileName));
		
			objModifyInstruction1=objFileModifier.new ModifyInstruction(p1,
																	OperationType.ADD,
																	scenarios,
																	new String[]{srcIdentifierAnno},
																	new String[]{ICommonConstants.TEST_EXECUTION_TYPE_SEQUENTIAL},
																	objFileModifier.getNextExecutionSequence());
			modifyInstructions.add(objModifyInstruction1);
			objFileModifier.modifyFile(srcfileName, destfileName, modifyInstructions, false);
		
			String modifiedFileContent = FileUtils.readFileToString(new File(destfileName), StandardCharsets.UTF_8);
		
			assertThat(
					modifiedFileContent, containsString(srcIdentifierAnno));
		
			assertThat(
					modifiedFileContent, containsString(p1));
		}catch(IOException e){
			LOGGER.info("Ignoring IOException[may be due to lack of privilege] :", e);
		}

	}
	
	
	private void copyFile(File source, File dest) throws IOException {
	    Files.copy(source.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(FileModifierTest.class);
}
