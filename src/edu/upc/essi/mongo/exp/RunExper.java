package edu.upc.essi.mongo.exp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
//import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.LoggerContext;

public class RunExper {

	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost/postgres";
		String user = "postgres";
		String password = "user";
//		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//		Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
//		rootLogger.setLevel(Level.ERROR);
		ArrayList<Exper> list = new ArrayList<>();

		list.add(new Exper("40-13m", 40, 13000000));
		list.add(new Exper("80-13m-2", 40 * 2, 13000000 / 2));
		list.add(new Exper("160-13m-4", 40 * 4, 13000000 / 4));
		list.add(new Exper("320-13m-8", 40 * 8, 13000000 / 8));
		list.add(new Exper("640-13m-16", 40 * 16, 13000000 / 16));

		list.add(new Exper("80-2m", 80, 2000000));
		list.add(new Exper("80-4m", 80, 2000000 * 2));
		list.add(new Exper("80-8m", 80, 2000000 * 4));
		list.add(new Exper("80-16m", 80, 2000000 * 8));
		list.add(new Exper("80-32m", 80, 2000000 * 16));
		list.add(new Exper("80-64m", 80, 2000000 * 32));

//		list.add(new Exper("40-13m", 40, 13000000));
		list.add(new Exper("80-13m", 40 * 2, 13000000));
		list.add(new Exper("160-13m", 40 * 4, 13000000));
		list.add(new Exper("320-13m", 40 * 8, 13000000));
		list.add(new Exper("640-13m", 40 * 16, 13000000));

		try {
			Connection conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO public.\"NewExp\"(stname, iter, run, col) VALUES (?, ?, ?, ?::JSON);";
			PreparedStatement ps = conn.prepareStatement(sql);
			for (Exper exper : list) {
				System.out.println(exper.name);
				List l1 = CSVUtils.fillIds(Const.ID_BASE + exper.name);

				for (int k = 0; k < 10; k++) {
					System.out.println(k);
					ProcessBuilder p1 = new ProcessBuilder(Const.MONGOD_LOC, "--config", Const.CONFIG_LOC, "--dbpath",
							Const.FOLDER_BASE + exper.name, "--bind_ip_all", "--fork", "--logpath", Const.LOG_LOC);
					Process p;
					p = p1.start();
					int retval1 = p.waitFor();
					Mongo mongo = new Mongo("localhost", 27017);
					DB db = mongo.getDB("final");// movies
					DBCollection collection = db.getCollection(exper.name);
					Random r = new Random();
					for (int j = 0; j < 50000; j++) {
						Thread.sleep(10);
						DBObject n = collection.findOne(new BasicDBObject("_id", l1.get(r.nextInt(l1.size()))));

						if (j % 100 == 0 && j > 2000) {
							CommandResult resultSet = collection.getStats();
							ps.setString(1, exper.name);
							ps.setInt(2, j);
							ps.setInt(3, k);
							ps.setObject(4, resultSet.toJson());
							ps.addBatch();
							if (j % 10000 == 0) {
								ps.executeBatch();
								ps = conn.prepareStatement(sql);

							}

						}
					}
					ps.executeBatch();
					ps = conn.prepareStatement(sql);
					mongo.close();
					ProcessBuilder p2 = new ProcessBuilder("mongo", "localhost:27017/admin", "--eval",
							"db.shutdownServer()");
					Process p3 = p2.start();
					int retval2 = p3.waitFor();

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
