package uk.ac.manchester.data_examples;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

public class Annotate {
	
	Connection conn = null;
	public Jdbc3PoolingDataSource source = ConnectionManager.getSource();
	int num_data_examples;

	public Annotate(int _num_data_examples) {
		// TODO Auto-generated constructor stub
		this.num_data_examples = _num_data_examples;
	}
	
	
	public void computeGTAnnotation() {
		
		String sql_1 = "select id_so from so";
		String sql_2 = "select count(*) from (select d.id from de d, feedback_gt f where (d.id_so = ?) and (f.expected = true) and (d.id = f.id)) as tab";
		String sql_3 = "select count(*) from (select d.id from de d, feedback_gt f where (d.id_so = ?) and (f.expected = false) and (d.id = f.id)) as tab";
		String sql_4 = "insert into annotation_gt values(?,?,?,?)";
		String sql_5 = "select id_so from annotation_gt where precision != 'NaN'order by precision desc limit 3";
		String sql_6 = "insert into top_k_precision_gt values(?,?)";
		String sql_7 = "select id_so from annotation_gt where recall != 'NaN' order by recall desc limit 3";
		String sql_8 = "insert into top_k_recall_gt values(?,?)";
		String sql_9 = "select id_so from annotation_gt where f_score != 'NaN' order by f_score desc limit 3";
		String sql_10 = "insert into top_k_f_score_gt values(?,?)";
		
	    java.sql.Statement st_1 = null, st_5 = null, st_7 = null, st_9 = null;
	    java.sql.PreparedStatement st_2 = null, st_3 = null, st_4 = null, st_6 = null, st_8 = null, st_10 = null;
	    java.sql.ResultSet rs_1 = null, rs_2 = null, rs_3 = null, rs_5 = null, rs_7 = null, rs_9 = null;
	    
	    int id_so;
		int tp, fp;
		double precision, recall, f_score;
	    
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st_1 = conn.createStatement();
			rs_1 = st_1.executeQuery(sql_1);
			
			while (rs_1.next()) {
				
				id_so = rs_1.getInt(1);
				
				st_2 = conn.prepareStatement(sql_2);
				st_2.setInt(1, id_so);
				rs_2 = st_2.executeQuery();
				rs_2.next();
				tp = rs_2.getInt(1);
				
				st_3 = conn.prepareStatement(sql_3);
				st_3.setInt(1, id_so);
				rs_3 = st_3.executeQuery();
				rs_3.next();
				fp = rs_3.getInt(1);

				precision = new Double(tp)/new Double(tp+fp);
				recall = new Double(tp)/new Double(num_data_examples);
				f_score = (2 * precision * recall)/(precision + recall);
				
				st_4 = conn.prepareStatement(sql_4);
				st_4.setInt(1, id_so);
				st_4.setDouble(2, precision);
				st_4.setDouble(3, recall);
				st_4.setDouble(4, f_score);
				st_4.execute();			
	
				
			}
			
			st_5 = conn.createStatement();
			rs_5 = st_5.executeQuery(sql_5);
			st_6 = conn.prepareStatement(sql_6);
			int i = 1;
			while (rs_5.next()) {
				st_6.setInt(1, rs_5.getInt(1));
				st_6.setInt(2, i);
				st_6.execute();
				i++;
			}
			
			st_7 = conn.createStatement();
			rs_7 = st_7.executeQuery(sql_7);
			st_8 = conn.prepareStatement(sql_8);
			i = 1;
			while (rs_7.next()) {
				st_8.setInt(1, rs_7.getInt(1));
				st_8.setInt(2, i);
				st_8.execute();
				i++;
			}
			
			st_9 = conn.createStatement();
			rs_9 = st_9.executeQuery(sql_9);
			st_10 = conn.prepareStatement(sql_10);
			i = 1;
			while (rs_9.next()) {
				st_10.setInt(1, rs_9.getInt(1));
				st_10.setInt(2, i);
				st_10.execute();
				i++;
			}
			
			
			st_1.close();
			st_2.close();
			st_3.close();
			st_4.close();
			st_5.close();
			st_6.close();
			st_7.close();
			st_8.close();
			st_9.close();
			st_10.close();
			rs_1.close();
			rs_2.close();
			rs_3.close();
			rs_5.close();
			rs_7.close();
			rs_9.close();
			conn.close();
			
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
		
		
		
	}
	
	
	public void generateFeedbackRandomly(int num_feedback) {
		
		String sql = "insert into feedback (select * from feedback_gt where id not in (select id from feedback) order by random() limit "+num_feedback+")";
		
		java.sql.Statement st = null;
		
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st = conn.createStatement();
			st.execute(sql);
			
			st.close();
			conn.close();
			
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
			

		
	}
	
	
	public void propagateFeedback() {
		
		String sql_1 = "insert into feedback (select distinct tab.id_2, fd.expected  from (select * from matching_input intersect select * from matching_output) tab, feedback fd where (fd.id = tab.id_1) and (tab.id_2 not in (select id from feedback)))";
		String sql_2 = "insert into feedback (select distinct tab.id_1, fd.expected  from (select * from matching_input intersect select * from matching_output) tab, feedback fd where (fd.id = tab.id_2) and (tab.id_1 not in (select id from feedback)))";
		String sql_3 = "insert into feedback (select distinct tab.id_1, false  from (select * from matching_input except select * from matching_output) tab, feedback fd where (fd.id = tab.id_2) and (tab.id_1 not in (select id from feedback)) and fd.expected)";
		
		java.sql.Statement st_1 = null, st_2 = null, st_3 = null;

		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st_1 = conn.createStatement();
			st_2 = conn.createStatement();
			st_3 = conn.createStatement();
			
			st_1.execute(sql_1);
			st_2.execute(sql_2);
			st_3.execute(sql_3);
		
			st_1.close();
			st_2.close();
			st_3.close();
			conn.close();
			
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
		
		
	}

	/*
	 * This method is not yet fully implemented
	 */
	public void computeAnnotation(int iteration) {
		
		String sql_1 = "select id_so from so";
		String sql_2 = "select count(*) from (select d.id from de d, feedback f where (d.id_so = ?) and (f.expected = true) and (d.id = f.id)) as tab";
		String sql_3 = "select count(*) from (select d.id from de d, feedback f where (d.id_so = ?) and (f.expected = false) and (d.id = f.id)) as tab";
		String sql_4 = "select count(*) from de_gt where id in (select id from feedback)";
		String sql_4_1 = "insert into annotation values(?,?,?,?,?)";
		
		String sql_5 = "select id_so from annotation where precision != 'NaN'and iteration = "+iteration+" order by precision desc limit 3";
		String sql_6 = "insert into top_k_precision values(?,?,?)";
		String sql_7 = "select id_so from annotation where recall != 'NaN' and iteration = "+iteration+" order by recall desc limit 3";
		String sql_8 = "insert into top_k_recall values(?,?,?)";
		String sql_9 = "select id_so from annotation where f_score != 'NaN' and iteration = "+iteration+" order by f_score desc limit 3";
		String sql_10 = "insert into top_k_f_score values(?,?,?)";
		
	    java.sql.Statement st_1 = null, st_4 = null, st_5 = null, st_7 = null, st_9 = null;
	    java.sql.PreparedStatement st_2 = null, st_3 = null, st_4_1 = null, st_6 = null, st_8 = null, st_10 = null;
	    java.sql.ResultSet rs_1 = null, rs_2 = null, rs_3 = null, rs_4 = null, rs_5 = null, rs_7 = null, rs_9 = null;
	    
	    int id_so;
		int tp, fp, expected;
		double precision, recall, f_score;
	    
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st_4 = conn.createStatement();
			rs_4 = st_4.executeQuery(sql_4);
			rs_4.next();
			expected = rs_4.getInt(1);
			
			st_1 = conn.createStatement();
			rs_1 = st_1.executeQuery(sql_1);
			
			while (rs_1.next()) {
				
				id_so = rs_1.getInt(1);
				
				st_2 = conn.prepareStatement(sql_2);
				st_2.setInt(1, id_so);
				rs_2 = st_2.executeQuery();
				rs_2.next();
				tp = rs_2.getInt(1);
				
				st_3 = conn.prepareStatement(sql_3);
				st_3.setInt(1, id_so);
				rs_3 = st_3.executeQuery();
				rs_3.next();
				fp = rs_3.getInt(1);

				precision = new Double(tp)/new Double(tp+fp);
				recall = new Double(tp)/new Double(expected);
				f_score = (2 * precision * recall)/(precision + recall);
				
				st_4_1 = conn.prepareStatement(sql_4_1);
				st_4_1.setInt(1, iteration);
				st_4_1.setInt(2, id_so);
				st_4_1.setDouble(3, precision);
				st_4_1.setDouble(4, recall);
				st_4_1.setDouble(5, f_score);
				st_4_1.execute();				
				
			}
			
			st_5 = conn.createStatement();
			rs_5 = st_5.executeQuery(sql_5);
			st_6 = conn.prepareStatement(sql_6);
			int i = 1;
			while (rs_5.next()) {
				//System.out.println("iteration: "+iteration+", operation: "+rs_5.getInt(1));
				st_6.setInt(1, iteration);
				st_6.setInt(2, rs_5.getInt(1));
				st_6.setInt(3, i);
				st_6.execute();
				i++;
			}
			
			st_7 = conn.createStatement();
			rs_7 = st_7.executeQuery(sql_7);
			st_8 = conn.prepareStatement(sql_8);
			i = 1;
			while (rs_7.next()) {
				st_8.setInt(1, iteration);
				st_8.setInt(2, rs_7.getInt(1));
				st_8.setInt(3, i);
				st_8.execute();
				i++;
			}
			
			st_9 = conn.createStatement();
			rs_9 = st_9.executeQuery(sql_9);
			st_10 = conn.prepareStatement(sql_10);
			i = 1;
			while (rs_9.next()) {
				st_10.setInt(1, iteration);
				st_10.setInt(2, rs_9.getInt(1));
				st_10.setInt(3, i);
				st_10.execute();
				i++;
			}
			
			
			st_1.close();
			st_2.close();
			st_3.close();
			st_4.close();
			st_4_1.close();
			st_5.close();
			st_6.close();
			st_7.close();
			st_8.close();
			st_9.close();
			st_10.close();
			rs_1.close();
			rs_2.close();
			rs_3.close();
			rs_5.close();
			rs_7.close();
			rs_9.close();
			conn.close();	
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
		
		
		
	}
	
	public void annotate(int num_feedback, int num_iteration) {
		
		for (int i = 0; i< num_iteration; i++) {
			this.generateFeedbackRandomly(num_feedback);
			this.computeAnnotation(i);
		}
		
		
	}
	
	public void annotateWithInference(int num_feedback, int num_iteration) {
		
		for (int i = 0; i< num_iteration; i++) {
			this.generateFeedbackRandomly(num_feedback);
			this.propagateFeedback();
			this.computeAnnotation(i);
		}
		
		
	}
	
	public void StoreAnnotationsAndInitialize(int run, int dataset, boolean inference) {
		
		String table_1 = "a_gt_d"+dataset;
		if (inference) table_1 = table_1 + "_i";
		
		String table_2 = "a_d"+dataset;
		if (inference) table_2 = table_2 + "_i";
		
		String table_3 = "tk_p_gt_d"+dataset;
		if (inference) table_3 = table_3 + "_i";
		
		String table_4 = "tk_r_gt_d"+dataset;
		if (inference) table_4 = table_4 + "_i";
		
		String table_5 = "tk_f_gt_d"+dataset;
		if (inference) table_5 = table_5 + "_i";
		
		String table_6 = "tk_p_d"+dataset;
		if (inference) table_6 = table_6 + "_i";
		
		String table_7 = "tk_r_d"+dataset;
		if (inference) table_7 = table_7 + "_i";
		
		String table_8 = "tk_f_d"+dataset;
		if (inference) table_8 = table_8 + "_i";
		
		
		if (run == 1) {
		this.executeQuery("create table "+table_1+" as (select "+run+" as run, t.* from annotation_gt t)");
		this.executeQuery("create table "+table_2+" as (select "+run+" as run, t.* from annotation t)");
		this.executeQuery("create table "+table_3+" as (select "+run+" as run, t.* from top_k_precision_gt t)");
		this.executeQuery("create table "+table_4+" as (select "+run+" as run, t.* from top_k_recall_gt t)");
		this.executeQuery("create table "+table_5+" as (select "+run+" as run, t.* from top_k_f_score_gt t)");
		this.executeQuery("create table "+table_6+" as (select "+run+" as run, t.* from top_k_precision t)");
		this.executeQuery("create table "+table_7+" as (select "+run+" as run, t.* from top_k_recall t)");
		this.executeQuery("create table "+table_8+" as (select "+run+" as run, t.* from top_k_f_score t)");
		}
		
		if (run > 1) {
		this.executeQuery("insert into "+table_1+" (select "+run+" as run, t.* from annotation_gt t)");
		this.executeQuery("insert into "+table_2+"  (select "+run+" as run, t.* from annotation t)");
		this.executeQuery("insert into  "+table_3+"  (select "+run+" as run, t.* from top_k_precision_gt t)");
		this.executeQuery("insert into  "+table_4+"  (select "+run+" as run, t.* from top_k_recall_gt t)");
		this.executeQuery("insert into  "+table_5+"  (select "+run+" as run, t.* from top_k_f_score_gt t)");
		this.executeQuery("insert into  "+table_6+"  (select "+run+" as run, t.* from top_k_precision t)");
		this.executeQuery("insert into  "+table_7+"  (select "+run+" as run, t.* from top_k_recall t)");
		this.executeQuery("insert into  "+table_8+"  (select "+run+" as run, t.* from top_k_f_score t)");
		}
		
		
		this.executeQuery("delete from feedback");
		this.executeQuery("delete from annotation");
		this.executeQuery("delete from top_k_precision");
		this.executeQuery("delete from top_k_recall");
		this.executeQuery("delete from top_k_f_score");
		
	}
	
	public void executeQuery(String query) {
		
		java.sql.Statement st = null;
		
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st = conn.createStatement();
			st.execute(query);
			
			st.close();
			conn.close();
		
		
		}catch (SQLException s){
		System.err.println("Error while trying to execute the query");
		s.printStackTrace();
		}	
		
	}

	
	public void experiment(int num_feedback, int num_iterations, int num_runs, int dataset, boolean inference) {
		
		if (inference) {
		
			for (int i = 1; i <= num_runs; i++) {
			
				System.out.println("run: "+i);
						
				this.annotateWithInference(num_feedback, num_iterations);
				this.StoreAnnotationsAndInitialize(i, dataset, true);
			
			}	
		}
		
		else {
			
			for (int i = 1; i <= num_runs; i++) {
				
				System.out.println("run: "+i);
				this.annotate(num_feedback, num_iterations);
				this.StoreAnnotationsAndInitialize(i, dataset, false);
				
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int num_data_examples = 50;
		int iteration = 1;
		int num_feedback = 1;
		int num_iterations = 100;
		int num_runs = 10;
		int dataset = 6;
		boolean inference = true;
		
		Annotate ann = new Annotate(num_data_examples);
		//ann.computeGTAnnotation();
		//ann.propagateFeedback();
		//ann.computeAnnotation(iteration);
		//ann.generateFeedbackRandomly(num_feedback);
		//ann.annotate(num_feedback, num_iterations);
		ann.experiment(num_feedback, num_iterations, num_runs, dataset, inference);
		
		

	}

}
