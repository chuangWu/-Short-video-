package test;
import redis.clients.jedis.Jedis;

public class testRedis {
	/**
	 * 测试redis 是否连接成功
	 * @author Nocol
	 */
		public static void main(String[] args) {
			Jedis jedis=new Jedis("192.168.33.128",6379);
			System.out.println(jedis.ping());
			jedis.close();
	}

}
