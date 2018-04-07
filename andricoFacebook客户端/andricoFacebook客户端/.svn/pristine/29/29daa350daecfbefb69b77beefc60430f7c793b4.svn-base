 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andjax.http;

import org.apache.http.HttpResponse;

import android.os.Handler;

public class HttpResponseByHandlerDecorator extends HttpResponseRunnable {
    private final Handler mHandler;
    private final HttpResponseRunnable mRunnable;

    public HttpResponseByHandlerDecorator(Handler handler, HttpResponseRunnable runnable) {
        assert (handler != null);
        this.mHandler = handler;
        this.mRunnable = runnable;
    }

    @Override
    public HttpResponse getResponse() {
        return this.mRunnable.getResponse();
    }

    @Override
    public void run() {
        this.mHandler.post(this.mRunnable);
    }

    @Override
    public void setResponse(HttpResponse response) {
        this.mRunnable.setResponse(response);
    }
}
