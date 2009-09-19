/*
 * Copyright (C) IBM Corp. 2008.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.jaql;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * 
 */
public class TestHBase extends JaqlBaseTestCase
{

//  private static final Log LOG        = LogFactory.getLog(TestHBase.class
//                                          .getName());
//  private MiniMRCluster    m_mr       = null;
//  private MiniHBaseCluster m_db       = null;
//  private MiniDFSCluster   m_fs       = null;
//  public static String     dataPrefix = "jaqlTest";
//  public static String     tempDir    = "/temp";

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.JaqlBaseTestCase#setUp()
   */
  @Override
  protected void setUp() throws IOException
  {
    if (1==1) throw new IOException("HBase test disabled; port to 0.20.0 required");
//    setFilePrefix("hbase");
//
//    if (!"cluster".equals(System.getProperty("test.deployment")))
//    {
//      HBaseConfiguration conf = new HBaseConfiguration();
//
//      //    setup conf according to the Hadoop version
//      String vInfo = VersionInfo.getVersion();
//      if (vInfo.indexOf("0.18") >= 0)
//      {
//        conf.set(HConstants.MASTER__ADDRESS, "local");
//      }
//      else if (vInfo.indexOf("0.15") >= 0)
//      {
//        conf.set(HConstants.MASTER_ADDRESS, "localhost:0");
//        conf.set(HConstants.REGIONSERVER_ADDRESS, "localhost:0");
//      }
//      else
//      {
//        throw new RuntimeException("Unsupported Hadoop version: " + vInfo);
//      }
//      //conf.set(HConstants.MASTER_ADDRESS, "localhost:0");
//      //conf.set(HConstants.REGIONSERVER_ADDRESS, "localhost:0");
//      conf.set("hbase.regionserver.msginterval", "250");
//      //conf.set("hbase.regionserver.handler.count", "2");
//      conf.set("hbase.master.meta.thread.rescanfrequency", "30000");
//      conf.setInt("hbase.master.lease.period", 10 * 1000);
//      //conf.set("hbase.regionserver.lease.period", "3000");
//      conf.setInt("hbase.master.lease.thread.wakefrequency", 5 * 1000);
//      conf.setLong("hbase.client.pause", 15 * 1000);
//
//      final int numNodes = 4;
//
//      m_fs = new MiniDFSCluster(conf, numNodes, true, (String[])null);
//      FileSystem filesystem = m_fs.getFileSystem();
//      conf.set("fs.default.name", filesystem.getUri().toString());
//      Path parentdir = filesystem.getHomeDirectory();
//      conf.set(HConstants.HBASE_DIR, parentdir.toString());
//      filesystem.mkdirs(parentdir);
//      FSUtils.setVersion(filesystem, parentdir);
//      
//      try
//      {
//        m_db = new MiniHBaseCluster(conf, numNodes);
//        setupOverride(conf);
//      }
//      catch (Exception e)
//      {
//        LOG.warn("Could not start HBase cluster due to exception", e);
//      }
//      catch (Error er)
//      {
//        LOG.warn("Could not start HBase cluster due to error", er);
//      }
//
//      
//      m_mr = new MiniMRCluster(numNodes, filesystem.getUri().toString(), 1);
//
//      try
//      {
//        TestMapReduce.setupOverride(m_mr.createJobConf(), conf);
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace(System.out);
//        System.out.flush();
//        fail("could not setup map-reduce override");
//      }
//    }
//    Configuration conf = new Configuration();
//    FileSystem fs = FileSystem.get(conf);
//
//    // make the home directory if it does not exist
//    Path hd = fs.getWorkingDirectory();
//    if (!fs.exists(hd)) fs.mkdirs(hd);
//
//    // make a data directory if it does not exist
//    Path p = new Path(dataPrefix);
//    if (fs.exists(p))
//    {
//      LOG.info("directory: " + p + ", exists");
//      fs.delete(p, true);
//    }
//    fs.mkdirs(p);
//
//    // make a tmp directory if it does not exist
//    Path t = new Path(tempDir);
//    if (!fs.exists(t))
//    {
//      fs.mkdirs(t);
//    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.JaqlBaseTestCase#tearDown()
   */
  @Override
  protected void tearDown() throws IOException
  {
//    if (m_mr != null)
//    {
//      m_mr.shutdown();
//    }
//    if (m_db != null)
//    {
//      m_db.shutdown();
//    }
//    if ("cluster".equals(System.getProperty("test.deployment")))
//    {
//      UtilForTest.cleanUpHDFS(dataPrefix);
//      UtilForTest.cleanUpHBase(dataPrefix);
//      UtilForTest.cleanUpHDFS(tempDir);
//    }
  }

  /**
   * @param conf
   * @throws IOException
   */
  static void setupOverride(HBaseConfiguration conf) throws IOException
  {
//    File overrideDir = new File(System.getProperty("hadoop.conf.override"));
//    // assume that override dir exists for unit tests
//    if (!overrideDir.exists()) fail("hadoop-override dir must exist");
//
//    // write out the JobConf from MiniMR to the override dir
//    File f = new File(overrideDir.getCanonicalPath() + File.separator
//        + "hbase-default.xml");
//    f.deleteOnExit();
//    DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
//    conf.write(out);
//    out.flush();
//    out.close();
  }
}
