package com.tradeengine.tradeengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class TradeengineApplication {

	public static void main(String[] args) {

		ValidatedOrder validatedOrder = new ValidatedOrder("1011","IBM","1.5","1000","SELL");
		String validatedOrderString = Utility.convertToString(validatedOrder);
		new Jedis().lpush("makeorder",validatedOrderString);
		System.out.println(validatedOrderString);
		SpringApplication.run(TradeengineApplication.class, args);
//	MAKE ORDERBOOK REQUEST
		new Thread(new Runnable() {
			Jedis jedis = new Jedis();
			@Override
			public void run() {
	    		while (true){
					String data = jedis.rpop("makeorder");
					if(data == null) continue;
					ValidatedOrder validatedOrder = Utility.convertToObject(data,ValidatedOrder.class);
					jedis.set(validatedOrder.id,data);
					jedis.lpush("monitorqueue",validatedOrder.id);
					OrderBookRequest orderBookRequest = new OrderBookRequest(
							validatedOrder.id,
							validatedOrder.product,
							validatedOrder.side
					);
					String requestString = Utility.convertToString(orderBookRequest);
					jedis.lpush("exchange1-orderrequest",requestString);
					jedis.lpush("exchange2-orderrequest",requestString);
				}
			}
		}).start();





//	MONITOR QUEUE
		new Thread(new Runnable() {
			Jedis jedis = new Jedis();
			@Override
			public void run() {
				while (true){
					String data = jedis.rpop("monitorqueue");
					if(data == null) continue;
					if(jedis.llen(data+"orderbook") == 2){

						PendingOrder[] pendingOrderList1 = Utility.convertToObject(jedis.rpop(data+"orderbook"),PendingOrder[].class);
						PendingOrder[] pendingOrderList2 = Utility.convertToObject(jedis.rpop(data+"orderbook"),PendingOrder[].class);

						List<PendingOrder> pendingOrderList = new ArrayList<>();

						pendingOrderList.addAll(Arrays.asList(pendingOrderList1));
						pendingOrderList.addAll(Arrays.asList(pendingOrderList2));

						ValidatedOrder validatedOrder = Utility.convertToObject( jedis.get(data), ValidatedOrder.class);


						jedis.lpush("makeorder"+"exchange1")


					}else{
						jedis.lpush("monitorqueue",data);
					}
				}
			}
		}).start();


	}



}



