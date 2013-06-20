package utilities;


public class str_search {

	/**
	 * @param args
	 */
	
	 // TODO Auto-generated method stub
	
	public static String[] EXAMPLE_TEST = {
			 "Badrul M. Sarwar , Joseph A. Konstan , Al Borchers , Jon Herlocker , Brad Miller , John Riedl, Using filtering agents to improve prediction quality in the GroupLens research collaborative filtering system, Proceedings of the 1998 ACM conference on Computer supported cooperative work, p.345-354, November 14-18, 1998, Seattle, Washington, United States",
			 "Zijlstra, F.R.H., R.A. Roe, A.B. Leonora and I. Krediet. Temporal Factors in Mental Work: Effects of Interrupted Activities. Journal of Occupational and Organizational Psychology, 72, 163-185, 1999. pp 1234-1254",
			 "Wilson, G.M. and M.A. Sasse. The Head or the Heart?: Measuring the Impact of Media Quality. CHI, 2000, 117-118",
			 "Michailidou, E., Harper, S., and Bechhofer, S. Visual Complexity and Aesthetic Perception ofWeb Pages. Proc. Design of Communication (2008), 215�224",
			 "J. Goecks and J. Shavlik. Learning users� interests by unobtrusively observing their normal behavior. Proc. IUI �00, 129�132.",
			 "K. Wang, T. Walker, and Z. Zheng. PSkip: Estimating relevance ranking quality from web search click- through data. Proc. KDD '09, 1355�1364.",
			 "Zhai, S., Sue, A. and Accot, J., Movement model, hits distribution and learning in virtual Keyboarding. Proc. CHI 2002: ACM Conference on Human Factors in Computing Systems, CHI Letters 4(1), 2002, ACM, 17- 24.",
			 "Zhai, S., Kristensson, P.-O. and Smith, B.A. In Search of Effective Text Input Interfaces for Off the Desktop Computing. Interacting with Computers, 16 (3). 2004, to appear.",
			 "Zhai, S. and Kristensson, P.-O., Shorthand Writing on Stylus Keyboard. Proc. CHI 2003, ACM Conference on Human Factors in Computing Systems, CHI Letters 5(1), 2003, ACM, 97-104.",
			 "Accot, J. and Zhai, S., Beyond Fitts' law: models for trajectory-based HCI tasks. Proc. CHI 1997: ACM Conference on Human Factors in Computing Systems, 1997, ACM, 295-302.",
			 "Basu, C., Hirsh, H., and Cohen, W. (1998). Recommendation as Classification: Using Social and Content-based Information in Recommendation. In Recommender System Workshop'98. pp. 11-15.",
			 "Sarwar, B. M., Karypis, G., Konstan, J. A., and Riedl, J. (2000). Application of Dimensionality Reduction in Recommender System{A Case Study. InACM WebKDD 2000 Workshop."
	};
	public static void main(String[] args) {
		
		for (String ref : EXAMPLE_TEST) {
			String[] splitString = (ref.split("\\s+"));			
			for (String val : splitString) {
				//System.out.println(val);
			    boolean year=val.matches("(\\d{4})(,?)(.?)");
			   	if (year){
					System.out.println(val.substring(0,4));
					//System.out.println(val);
				}
				
				else if(year=val.matches("[(]\\d{4}[)],?.?")){
				//else if(val.charAt(0)== '(' && val.charAt(val.length()-2) == ')'){
					System.out.println(val.substring(1, 5));
			    }
				
			   	//else if(year=val.matches("[']\\d{2},?.? | [�]\\d{2},?.?")){    
				else if((val.charAt(0)== '\'' | val.charAt(0)== '�') && val.charAt(val.length()-1) == ','){
					System.out.println("20" + val.substring(1, 3));
			    }
			   	
			   	if(year) break;
			}   
		}			  
	}
}