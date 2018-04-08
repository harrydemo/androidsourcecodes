/*
 * [��������] Android ���ֲ�����
 * [�ο�����] http://code.google.com/p/rockon-android/ 
 * [��ԴЭ��] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmobileapp.rockplayer;

public class ArtistEvent{
	public String venue = null;
	public String city = null;
	public String country = null;
	public String date = null;
	public double dateInMillis = System.currentTimeMillis();
	public String time = null;
	public String artist = null;
	public double latitude = 0;
	public double longitude = 0;
	
	public ArtistEvent(){
		
	}
}