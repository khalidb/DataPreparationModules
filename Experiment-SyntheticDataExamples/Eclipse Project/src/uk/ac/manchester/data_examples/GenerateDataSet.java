package uk.ac.manchester.data_examples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;


public class GenerateDataSet {
	
	
	int de_id = 1;
	int id_so = 1;
	int step;
	int num_data_examples;
	int num_op_per_step;
	int diff_de;
	Connection conn = null;
	public Jdbc3PoolingDataSource source = ConnectionManager.getSource();
	
	public GenerateDataSet(int _num_data_examples, int _num_op_per_step, int _diff_de, int step) {
		
		num_data_examples = _num_data_examples;
		num_op_per_step = _num_op_per_step;
		diff_de = _diff_de;	
		this.step = step;
		
	}
	
	public void generateDataSet() {
		
		
		// reset database
		this.resetDataSet();
		
		// generate data ground examples 
		this.generate_gt_data_examples();
		
		// generate an operation with a precision and a recall of 1 having the same data examples as the ground truth ones
		this.generateOperationGT();
		
		// generate an operation with num_data_examples - diff_de true positives and diff_de false positives
		this.generateOperation(step);
		
		step++;
		
		// generate a second operation with num_data_examples - diff_de true positives and diff_de false positives
		this.generateOperation(step);
		
		step++;
		
		
		while ((step * this.diff_de) <= num_data_examples) {
			
			for (int i = 0; i < this.num_op_per_step; i++) {
				this.generateOperation(step);
				
			}
			
			step++;
			
		}
			
		
		
	}
	
	
	private void generateOperation(int step) {
		
		int tp = this.num_data_examples - (step * this.diff_de);
		int fp = step * this.diff_de;
		
		String sql_1 = "insert into so values("+id_so+")";
		String sql_2 = "select id from de_gt order by random()";
		java.sql.Statement st_1 = null, st_2 = null;
		ResultSet rs_2 =null;
		
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			st_1 = conn.createStatement();
			st_1.execute(sql_1);
			
			
			st_2 = conn.createStatement();
			rs_2 = st_2.executeQuery(sql_2);
			
			for (int i = 1; i<= tp; i++) {
				
				rs_2.next();
				this.generateTP(id_so,rs_2.getInt(1));
	
			}
			
			for (int i = 1; i<= fp; i++) {
	
				rs_2.next();
				this.generateFP(id_so,rs_2.getInt(1));	
				
			}
			
			id_so++;
			
			st_1.close();
			st_2.close();
			rs_2.close();
			conn.close();
			
			
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}				
		
		
		
		
		
		
	}
	
	
	private void generateFP(int idSo, int int1) {
		
		
		String sql_1 = "insert into de values("+de_id+","+id_so+")";
		String sql_2 = "insert into matching_input values("+de_id+","+int1+")";
		String sql_3 = "insert into feedback_gt values("+de_id+", false)";
		
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
			
			de_id++;
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
		
	}

	private void generateTP(int idSo, int int1) {
		
		
		String sql_1 = "insert into de values("+de_id+","+id_so+")";
		String sql_2 = "insert into matching_input values("+de_id+","+int1+")";
		String sql_3 = "insert into matching_output values("+de_id+","+int1+")";
		String sql_4 = "insert into feedback_gt values("+de_id+", true)";
		
		java.sql.Statement st_1 = null, st_2 = null, st_3 = null, st_4 = null;
		
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st_1 = conn.createStatement();
			st_2 = conn.createStatement();
			st_3 = conn.createStatement();
			st_4 = conn.createStatement();
		
			
			st_1.execute(sql_1);
			st_2.execute(sql_2);
			st_3.execute(sql_3);
			st_4.execute(sql_4);
			
			st_1.close();
			st_2.close();
			st_3.close();
			st_4.close();
			conn.close();
			
			de_id++;
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
		
	}

	private void generateOperationGT() {
		
		String sql_1 = "insert into so values ("+id_so+")";
		String sql_2 = "insert into de (select id, "+id_so+"from de_gt)"; 
		String sql_3 = "insert into feedback_gt (select id, true from de_gt)";
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
			
			id_so++;
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}	
		
	}

	private void generate_gt_data_examples() {
		
		String sql_1 = "insert into de_gt values(?)";
		String sql_2 = "insert into matching_input values(?,?)";
		String sql_3 = "insert into matching_output values(?,?)";
		
		java.sql.PreparedStatement st_1 = null, st_2 = null, st_3 = null;
		
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			
			st_1 = conn.prepareStatement(sql_1);
			st_2 = conn.prepareStatement(sql_2);
			st_3 = conn.prepareStatement(sql_3);
			
			do{
				st_1.setInt(1, de_id);
				st_1.execute();
				
				st_2.setInt(1, de_id);
				st_2.setInt(2, de_id);
				st_2.execute();
				
				st_3.setInt(1, de_id);
				st_3.setInt(2, de_id);
				st_3.execute();
				
				de_id++;
			} while (de_id <= num_data_examples); 				
			
			st_1.close();
			st_2.close();
			st_3.close();
			conn.close();
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the query");
			s.printStackTrace();
		}

		
	}

	public void resetDataSet(){
		
		java.sql.Statement st = null;
		String query = "delete from ";
		
		try  {
			if ((conn == null) || conn.isClosed() ) 	
				conn = source.getConnection();
			st = conn.createStatement();
			
			st.execute(query + "annotation");
			st.execute(query + "annotation_gt");
			st.execute(query + "de");
			st.execute(query + "de_gt");
			st.execute(query + "matching_input");
			st.execute(query + "matching_output");
			st.execute(query + "feedback");
			st.execute(query + "feedback_gt");
			st.execute(query + "so");	
			st.execute(query + "top_k_precision_gt");	
			st.execute(query + "top_k_recall_gt");	
			st.execute(query + "top_k_f_score_gt");	

			st.close();
			conn.close();
		
		}catch (SQLException s){
			System.err.println("Error while trying to execute the following query: "+query);
			s.printStackTrace();
		}
		
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num_data_examples = 50;
		int num_op_per_step = 20;
		int diff_de = 1;		
		int step = 1;
		
		GenerateDataSet main = new GenerateDataSet(num_data_examples, num_op_per_step, diff_de, step);
		
		main.generateDataSet();
		

	}

}
