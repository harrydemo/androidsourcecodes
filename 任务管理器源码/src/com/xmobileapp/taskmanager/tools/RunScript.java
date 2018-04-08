/*
 * [程序名称] Android 任务管理器
 * [作者] xmobileapp团队
 * [参考资料] http://code.google.com/p/freetaskmanager/ 
 * [开源协议] GNU General Public License v2 (http://www.gnu.org/licenses/old-licenses/gpl-2.0.html)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 */

package com.xmobileapp.taskmanager.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunScript {
    private String command;
    private String stdout;
    private String stderr;
    private int retvalue;

    public RunScript(String command) {
        this.command = command;
    }

    public static String runIt(String command) {
        return new RunScript(command).run();
    }

    public String run() {
        String sRet = "";
        try {
            final Process m_process = Runtime.getRuntime().exec(this.command);
            final StringBuilder sbread = new StringBuilder();
            Thread tout = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_process
                            .getInputStream()), 8192);
                    String ls_1 = null;
                    try {
                        while ((ls_1 = bufferedReader.readLine()) != null) {
                            sbread.append(ls_1).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            // TODO: 异常处理
                        }
                    }
                }
            });
            tout.start();
            final StringBuilder sberr = new StringBuilder();
            Thread terr = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_process
                            .getErrorStream()), 8192);
                    String ls_1 = null;
                    try {
                        while ((ls_1 = bufferedReader.readLine()) != null) {
                            sberr.append(ls_1).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            // TODO：异常处理
                        }
                    }
                }
            });
            terr.start();
            this.retvalue = m_process.waitFor();
            while (tout.isAlive()) {
                Thread.sleep(50);
            }
            if (terr.isAlive()) terr.interrupt();
            this.stdout = sbread.toString();
            this.stderr = sberr.toString();
            sRet = this.stdout + this.stderr;
        } catch (java.io.IOException ee) {
            System.err.println("RunScript have a IO error :" + ee.getMessage());
            return null;
        } catch (InterruptedException ie) {
            System.err.println("RunScript have a interrupte error:" + ie.getMessage());
            return null;
        } catch (Exception ex) {
            System.err.print("RunScript have a error :" + ex.getMessage());
            return null;
        }
        return sRet;
    }

    public String getCommand() {
        return command;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public int getRetvalue() {
        return retvalue;
    }

}
