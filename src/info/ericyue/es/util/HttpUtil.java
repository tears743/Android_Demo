package info.ericyue.es.util;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil{
	//public static final String BASE_URL="http://10.0.2.2:8080/ExpressSensorWeb/";
	public static final String BASE_URL="http://121.42.58.134:8080/ExpressSensorWeb/";
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url);
		return request;
	}
	public static HttpPost getHttpPost(String url){
		HttpPost request= new HttpPost(url);
		return request;
	}
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	public static String queryStringForPost(String url){
		HttpPost request = HttpUtil.getHttpPost(url);
		String result=null;		
		try{
			HttpResponse response =HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
				return result;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result="Õ¯¬Á“Ï≥£";
			return result;
		}catch(IOException e){
			e.printStackTrace();
			result="Õ¯¬Á“Ï≥£";
			return result;
		}
		return result;
	}
	public static String queryStringForGet(String url){
		HttpGet request =HttpUtil.getHttpGet(url);
		String result = null;		
		try{
			HttpResponse response=HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
				return result;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result="Õ¯¬Á“Ï≥£";
			return result;
		}catch(IOException e){
			e.printStackTrace();
			result="Õ¯¬Á“Ï≥£";
			return result;
		}
		return result;
	}
	public static String queryStringForPost(HttpPost request){
		String result = null;
		
		try{
			HttpResponse response=HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
				return result;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result="Õ¯¬Á“Ï≥£";
			return result;
		}catch(IOException e){
			e.printStackTrace();
			result="Õ¯¬Á“Ï≥£";
			return result;
		}
		return result;
	}
	public static String queryStatistics(int id,String queryType){
		String queryStr="id="+id+"&querytype="+queryType;
		String url=HttpUtil.BASE_URL+"servlet/QueryStatistics?"+queryStr;	
		return HttpUtil.queryStringForPost(url);
	}
	
	public static String queryChaufferRemind(String date,String fromAdd,
			String driver_type){
		String queryStr = "date="+date+"&fromAdd="+fromAdd+"&drivertype="+driver_type;
		
		String url=HttpUtil.BASE_URL+"servlet/QueryChaufferRemind?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	
	public static String queryCargoRemind(String date,String fromAdd,
			String toAdd){
		String queryStr = "date="+date+"&fromAdd="+fromAdd+"&toAdd="+toAdd;
		
		String url=HttpUtil.BASE_URL+"servlet/QueryCargoRemind?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	
	public static String insertChauffeurInfo(String chauffeur_name,String health_status,
			String drive_age,String drive_tyle,String area_where,String tel,
			String effect_date,String remark,String publish_date,String publish_name){
		String queryStr = "chauffeur_name="+chauffeur_name+"&health_status="+health_status+"&drive_age="+drive_age+"&drive_tyle="+drive_tyle+
				"&area_where="+area_where+"&tel="+tel+"&effect_date="+effect_date+"&remark="+remark+
				"&publish_date="+publish_date+"&publish_name="+publish_name;
		
		String url=HttpUtil.BASE_URL+"servlet/InsertChauffeurInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	
	public static String insertVehicleInfo(String userName,String vehicle_sd,String vehicle_dd,String pass_where,
			String License_number,String driving_type,String timely_or_long_time,String departure_time,String useful_expressions,
			String introducenew){
		String queryStr = "userName="+userName+"&vehicle_sd="+vehicle_sd+"&vehicle_dd="+vehicle_dd+"&pass_where="+pass_where+
				"&License_number="+License_number+"&driving_type="+driving_type+"&timely_or_long_time="+timely_or_long_time+"&departure_time="+departure_time+
				"&useful_expressions="+useful_expressions+"&introducenew="+introducenew;
		String url=HttpUtil.BASE_URL+"servlet/InsertVehicleInfo?"+queryStr;
		
		
		return HttpUtil.queryStringForPost(url);
	}
	
	public static String insertCargoInfo(String userName,String cargo_sd,String cargo_dd,String cargo_name,String cargo_weight,String introduce,
			String cargo_releasedate,String cargo_volume,String cargo_type,String info_effective,String transport_style,String transport_fee,String link_name,String link_phone
			,String notice){
		String queryStr="userName="+userName+"&cargo_sd="+cargo_sd+"&cargo_dd="+cargo_dd+"&" +
				"&cargo_name="+cargo_name+"&cargo_weight="+cargo_weight+"&introduce="+introduce+"&cargo_releasedate="+cargo_releasedate+
				"&cargo_volume="+cargo_volume + "&cargo_type="+cargo_type+ "&info_effective="+info_effective+ "&transport_style="+transport_style+ "&transport_fee="+transport_fee+
				"&link_name="+link_name+ "&link_phone="+link_phone+ "&notice="+notice+ "&cargo_name="+cargo_name;
		String url=HttpUtil.BASE_URL+"servlet/InsetCargoInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String insertRegisterInfo(String registName,String trueName,String passWord,String companyName,String role,String cellphone,String address,String email ){
		String queryStr="userName="+registName+"&trueName="+trueName+"&passWord="+passWord+"&" +
				"role="+role+"&cellphone="+cellphone+"&address="+address+"&email="+email+"&companyName="+companyName;
		String url=HttpUtil.BASE_URL+"servlet/InsertRegisterInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String  Query_chauff_info(String cartype,String chaufaddress){
		String queryStr ="drive_tyle="+cartype+"&"+"chaufaddress="+chaufaddress;
		String url=HttpUtil.BASE_URL+"servlet/QueryChaufInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String  Query_cargo_info(String cargo_sd,String cargo_dd){
		String queryStr ="cargo_sd="+cargo_sd+"&"+"cargo_dd="+cargo_dd;
		String url=HttpUtil.BASE_URL+"servlet/Query_cargo_info?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String queryTradeInfo(String via,String viavalue,String querytype){
		String queryStr="via="+via+"&viavalue="+viavalue+"&querytype="+querytype;
		String url=HttpUtil.BASE_URL+"servlet/QueryTradeInfo?"+queryStr;	
		return HttpUtil.queryStringForPost(url);
	}
	public static String queryRole(int id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryRole?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String queryForSend(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryForSend?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String queryCurrentLocation(){
		String url=HttpUtil.BASE_URL+"servlet/GetUserCurrentLocation";
		return HttpUtil.queryStringForPost(url);
	}
	public static String DeleteCurrentUserLocation(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/DeleteCurrentUserLocation?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateUserCurrentLocation(String id,String lng,String lat){
		String queryStr="id="+id+"&lng="+lng+"&lat="+lat;
		String url=HttpUtil.BASE_URL+"servlet/UpdateUserCurrentLocation?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QueryBaseInfo(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryBaseInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateTradeStatus(String number,String status){
		String queryStr="number="+number+"&status="+status;
		String url=HttpUtil.BASE_URL+"servlet/UpdateTradeStatus?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String GetWorkerTrace(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/GetWorkerGPSInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateStatistics(String id,String item,String mode){
		String queryStr="id="+id+"&item="+item+"&mode="+mode;
		String url=HttpUtil.BASE_URL+"servlet/UpdateStatistics?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateCash(String id,String value){
		String queryStr="id="+id+"&value="+value;
		String url=HttpUtil.BASE_URL+"servlet/UpdateCash?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QuerySystemInfo(String type){
		String queryStr="type="+type;
		String url=HttpUtil.BASE_URL+"servlet/QuerySystemInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String SentMsgDone(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/SentMsgDone?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateMsgDone(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/UpdateMsgDone?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QueryTradeProcessed(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryTradeProcessed?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}		
	public static String UpdateTradeProcessed(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/UpdateTradeProcessed?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
}