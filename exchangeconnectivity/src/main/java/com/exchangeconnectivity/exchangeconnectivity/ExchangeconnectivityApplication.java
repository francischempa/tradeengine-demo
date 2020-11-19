package com.exchangeconnectivity.exchangeconnectivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class ExchangeconnectivityApplication {
//	EXCHANGE 1
	public static void main(String[] args) {
		SpringApplication.run(ExchangeconnectivityApplication.class, args);
		new Thread(new Runnable() {
			Jedis jedis = new Jedis();
			@Override
			public void run() {
				while (true){
					String data = jedis.rpop("exchange1-orderrequest");
					if(data == null) continue;
					System.out.println(data);
					OrderBookRequest orderBookRequest = Utility.convertToObject(data,OrderBookRequest.class);
//					WebClient /orderbook/{product}/{side}'
					System.out.println("#1");
					String response ="[{\"product\":\"IBM\",\"quantity\":2,\"price\":0.23,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":60,\"price\":0.23,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":2,\"price\":0.11,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":2,\"price\":0.12,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":5,\"price\":0.09,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":5,\"price\":0.09,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":2,\"price\":0.11,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":50,\"price\":0.12,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":50,\"price\":0.12,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":5,\"price\":0.12,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":1234,\"price\":0.12,\"side\":\"BUY\",\"executions\":[],\"cumulatitiveQuantity\":0},{\"product\":\"IBM\",\"quantity\":50,\"price\":0.5,\"side\":\"BUY\",\"executions\":[{\"timestamp\":\"2020-11-19T15:33:01.266\",\"price\":0.5,\"quantity\":7}],\"cumulatitiveQuantity\":7}]";
					PendingOrder[] pendingOrders = Utility.convertToObject(response,PendingOrder[].class);
					if(pendingOrders == null){
						System.out.println("Pending orders nulll");
					}
					System.out.println("#2");
					for (int i = 0 ; i< pendingOrders.length;i++){
//						CONVERT TICKERS HERE
						pendingOrders[i].exchange = "exchange1";
					}
					System.out.println("#3");
					jedis.lpush(orderBookRequest.id + "orderbook",Utility.convertToString(pendingOrders));
					jedis.lpush(orderBookRequest.id + "orderbook",Utility.convertToString(pendingOrders));
				}
			}
		}).start();


//		MAKE ORDER
		new Thread(new Runnable() {
			Jedis jedis = new Jedis();
			@Override
			public void run() {
				while (true){
					String data = jedis.rpop("makeorderexchange1");
					if(data == null) continue;

					WebClient webClient;

				}
			}
		}).start();
	}

}
