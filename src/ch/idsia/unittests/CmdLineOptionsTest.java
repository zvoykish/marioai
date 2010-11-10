/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.unittests;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.utils.ParameterContainer;
import junit.framework.TestCase;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Aug 28, 2010
 * Time: 8:36:02 PM
 * Package: ch.idsia.unittests
 */

public class CmdLineOptionsTest extends TestCase
{
CmdLineOptions cmdLineOptions;

@BeforeTest
public void setUp()
{
    cmdLineOptions = new CmdLineOptions();
}

@AfterTest
public void tearDown()
{
}

@Test
public void testTotalNumberOfOptions() throws Exception
{
    assertEquals(47, cmdLineOptions.getTotalNumberOfOptions());
}

@Test
public void testAllOptionsHaveDefaults()
{
    assertEquals(ParameterContainer.getTotalNumberOfOptions(), ParameterContainer.getNumberOfAllowedOptions());
}

@Test
public void testSetArgs() throws Exception
{
    String args = "-ag ch.idsia.agents.controllers.human.HumanKeyboardAgent" +
            " -amico off" +
            " -echo off" +
            " -ewf on" +
            " -cgr 1.0" +
            " -mgr 1.0" +
            " -gv off" +
            " -gvc off" +
            " -i off" +
            " -ld 0" +
            " -ll 320" +
            " -ls 0" +
            " -lt 0" +
            " -fps 24" +
            " -mm 2" +
            " -pw off" +
            " -pr off" +
            " -rfh 19" +
            " -rfw 19" +
            " -srf off" +
            " -tl 200" +
            " -tc off" +
            " -vaot off" +
            " -vlx 0" +
            " -vly 0" +
            " -vis on" +
            " -vw 320" +
            " -vh 240" +
            " -zs 1" +
            " -ze 0" +
            " -lh 15" +
            " -lde off" +
            " -lca on" +
            " -lhs on" +
            " -ltb on" +
            " -lco on" +
            " -lb on" +
            " -lg on" +
            " -lhb off" +
            " -le 1111111111" +
            " -lf off";
    cmdLineOptions.setArgs(args);
//    TODO:TASK:[M] test all cases
}

@Test
public void testSetLevelEnemies()
{
    cmdLineOptions.setArgs("-le 1111111111");
    // TODO:TASK:[M] test various conditions
}

@Test
public void testSetMarioInvulnerable() throws Exception
{
    cmdLineOptions.setMarioInvulnerable(true);
    assertEquals(cmdLineOptions.isMarioInvulnerable(), true);
    cmdLineOptions.setArgs("-i off");
    assertEquals(cmdLineOptions.isMarioInvulnerable(), false);
}

@Test
public void testDefaultAgent()
{
    assertNotNull(cmdLineOptions.getAgent());
    assertEquals("ch.idsia.agents.controllers.human.HumanKeyboardAgent", cmdLineOptions.getAgentFullLoadName());
    assertEquals("HumanKeyboardAgent", cmdLineOptions.getAgent().getName());
}

@Test
public void testStop()
{
    this.cmdLineOptions.setArgs("-stop on");
    assertEquals(true, this.cmdLineOptions.isStopGamePlay().booleanValue());
    assertEquals(GlobalOptions.isGameplayStopped, this.cmdLineOptions.isStopGamePlay().booleanValue());
}

@Test
public void testReset()
{
    cmdLineOptions.setArgs("-echo on -rfw 21 -rfh 17");
    assertTrue(cmdLineOptions.isEcho());
    assertEquals(new Integer(21), cmdLineOptions.getReceptiveFieldWidth());
    assertEquals(new Integer(17), cmdLineOptions.getReceptiveFieldHeight());

    cmdLineOptions.reset();
    assertFalse(cmdLineOptions.isEcho());
    assertEquals(new Integer(19), cmdLineOptions.getReceptiveFieldWidth());
    assertEquals(new Integer(19), cmdLineOptions.getReceptiveFieldHeight());
}

}
