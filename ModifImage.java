import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModifImage {
	
	public ModifImage() {
		try {

		 //zapis do pliku zmodyfikowanego obrazu
			File ouptut;
			
			ouptut = new File("res/img1.jpg");
			ImageIO.write(modif1("res/2.jpg", 50,205, 102, 230), "jpg", ouptut);
			
			ouptut = new File("res/filter1.jpg");
			ImageIO.write(filtrUsredn("res/2.jpg"), "jpg", ouptut);
			
			ouptut = new File("res/filter2.jpg");
			ImageIO.write(filtrGaussa("res/2.jpg", 10), "jpg", ouptut);
			
		} catch (Exception e) {}
	}
	
	// 1.
		public BufferedImage modif1(String strImg, int a, int b, int c1, int d1) throws IOException {
			BufferedImage img = ImageIO.read(new File(strImg));
			int width = img.getWidth();
			int height = img.getHeight();
			
			for(int i=0; i<height; i++){
				 
				 for(int j=0; j<width; j++){
		
					 Color c = new Color(img.getRGB(j, i));
					 int red = (int)(c.getRed());
					 int green = (int)(c.getGreen());
					 int blue = (int)(c.getBlue());

					 red = (red - a) * (d1 - c1)/(b - a) + c1;
					 green = (green - a) * (d1 - c1)/(b - a) + c1;
					 blue = (blue - a) * (d1 - c1)/(b - a) + c1;
					
					 red = sprawdzenie(red);
					 green = sprawdzenie(green);
					 blue = sprawdzenie(blue);
		 
					 Color newColor = new Color(red, green,blue);
					 img.setRGB(j,i,newColor.getRGB());

				 }
			}
		
			return img;
			
		}
		
		// 2a. filtr usredniajacy
		public BufferedImage filtrUsredn(String strImg) throws IOException {
			BufferedImage img = ImageIO.read(new File(strImg));
			int width = img.getWidth();
			int height = img.getHeight();
			
			BufferedImage img1 = img;
			
			int[][] M = new int[3][3];
			M[0][0] = 1; M[0][1] = 1; M[0][2] = 1;
			M[1][0] = 1; M[1][1] = 1; M[1][2] = 1;
			M[2][0] = 1; M[2][1] = 1; M[2][2] = 1;
			
			for(int i=0; i<height; i++){
				 
				 for(int j=0; j<width; j++){
		
					 int pomoc_r = 0;
					 int pomoc_g = 0;
					 int pomoc_b = 0;

					 for(int k=-1; k<=1; k++){

						 for(int l=-1; l<=1; l++){
							 if(j+k >= 0 && i+l >=0 && j+k<width && i+l<height) {
							 Color c = new Color(img.getRGB(j+k, i+l));
							 int red = (int)(c.getRed());
							 int green = (int)(c.getGreen());
							 int blue = (int)(c.getBlue());
							 pomoc_r += red * M[k+1][l+1];
							 pomoc_g += green * M[k+1][l+1];
							 pomoc_b += blue * M[k+1][l+1];
							 }
						 }
					 }
					 
					 pomoc_r /= 9; pomoc_g /= 9; pomoc_b /= 9;
					 Color newColor = new Color(pomoc_r, pomoc_g, pomoc_b);
					 img1.setRGB(j,i,newColor.getRGB());
				 }
			}
		
			return img1;
			
		}
		
		// 2b. filtr Gaussa
		public BufferedImage filtrGaussa(String strImg,  double sigma) throws IOException {
			BufferedImage img = ImageIO.read(new File(strImg));
			int width = img.getWidth();
			int height = img.getHeight();
				
			int N;
			N = 15;
					
			double[][] M = new double[N*2+1][N*2+1];
					
			for(int n=-N; n<=N; n++){
				 for(int m=-N; m<=N; m++){
					 M[n+N][m+N] = Math.exp((-1)*(n*n+m*m)/(2*sigma*sigma)); 
					 //System.out.print("");
					 //System.out.format(Locale.ENGLISH,"%.5f%n",M[n+N][m+N]);
				 }
				 System.out.println("");
			}
					
								
			int[][] R = new int[width][height];
			int[][] G = new int[width][height];
			int[][] B = new int[width][height];
					
			for(int i=0; i<height; i++){
				 for(int j=0; j<width; j++){
				
					 double pomoc_r = 0;
					 double pomoc_g = 0;
					 double pomoc_b = 0;
							 
					 Color c = new Color(img.getRGB(j, i));
					 int red = (int)(c.getRed());
					 int green = (int)(c.getGreen());
					 int blue = (int)(c.getBlue());
							 
					 R[j][i] = red;
					 G[j][i] = green;
					 B[j][i] = blue;
							
					 double sum = 0;
						 
					 for(int k=-N; k<=N; k++){
						 for(int l=-N; l<=N; l++){
							 if(j+k >= 0 && i+l >=0 && j+k<width && i+l<height) {
								 Color c1 = new Color(img.getRGB(j+k, i+l));
								 int red1 = (int)(c1.getRed());
								 int green1 = (int)(c1.getGreen());
								 int blue1 = (int)(c1.getBlue());
								 pomoc_r += red1 * M[k+N][l+N];
								 pomoc_g += green1 * M[k+N][l+N];
								 pomoc_b += blue1 * M[k+N][l+N];
								 sum += M[k+N][l+N];
							 }
						 }
					 }
						 
					 pomoc_r /= sum; pomoc_g /= sum; pomoc_b /= sum;
							 
					 int red_new = (int)pomoc_r;
					 int green_new = (int)pomoc_g;
					 int blue_new = (int)pomoc_b;
				
					 R[j][i] = red_new;
					 G[j][i] = green_new;
					 B[j][i] = blue_new;
	
				 }
			}
				
			for(int f=0; f<height; f++){
				 for(int t=0; t<width; t++){
					Color newColor = new Color(R[t][f], G[t][f], B[t][f]);
					img.setRGB(t,f,newColor.getRGB());
				 }
			}

			return img;
		}
		
		int sprawdzenie(int x)
	    {
	        if (x > 255)
	            return 255;
	        else if (x < 0)
	            return 0;
	        else
	        	return x;
	    }

	public static void main(String[] args) {
		ModifImage obj = new ModifImage();
		
		
				int WIDTH = 1200, HEIGHT = 600;
				
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int x = screenSize.width / 2 - WIDTH / 2;
				int y = screenSize.height / 2 - HEIGHT / 2;
				
				  JFrame f=new JFrame("Image Processing");
				  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				  f.setBounds(x, y,WIDTH, HEIGHT);
				  
				  Image image = new ImageIcon("res/2.jpg").getImage();
				  Image image1 = new ImageIcon("res/img1.jpg").getImage();
				  Image image2 = new ImageIcon("res/filter1.jpg").getImage();
				  Image image3 = new ImageIcon("res/filter2.jpg").getImage();
				  
				  JLabel label1 = new JLabel(new ImageIcon(image));
			      label1.setBounds(0,0, 500,500);  
			      JLabel label2 = new JLabel(new ImageIcon(image1));
			      label2.setBounds(0,0, 500,500);
			      JLabel label3 = new JLabel(new ImageIcon(image2));
			      label3.setBounds(0,0, 500,500);
			      JLabel label4 = new JLabel(new ImageIcon(image3));
			      label4.setBounds(0,0, 500,500);  
			      
				  JPanel panel1=new JPanel();  
			      panel1.setBounds(20,20,500,500); 
		     
			      panel1.add(label1);
			  	      
			      JPanel panel2=new JPanel();  
			      panel2.setBounds(680,10,500,500);
			      
			      // Definiowanie przycisku1 do zmiany obrazów
				  JButton b1=new JButton("Skalowania");  
				  b1.setBounds(550,100,100,50); 
				  b1.addActionListener(new ActionListener()
				  {  
					  public void actionPerformed(ActionEvent e){ 
						  panel2.removeAll();
						  panel2.add(label2); 
				          f.repaint();
					  } 
				  });

				  // Definiowanie przycisku2 do zmiany obrazów
				  JButton b2=new JButton("Usredniajacy");  
				  b2.setBounds(550,200,100,50); 
				  b2.addActionListener(new ActionListener()
				  {  
					  public void actionPerformed(ActionEvent e){  
						  panel2.removeAll();
						  panel2.add(label3); 
				          f.repaint();  
					  } 
				  });
				  
				// Definiowanie przycisku3 do zmiany obrazów
				  JButton b3=new JButton("filtr Gaussa");  
				  b3.setBounds(550,300,100,50); 
				  b3.addActionListener(new ActionListener()
				  {  
					  public void actionPerformed(ActionEvent e){  
						  panel2.removeAll();
						  panel2.add(label4); 
				          f.repaint();  
					  } 
				  });
				  
				  
				  
				  f.add(panel1);
				  f.add(panel2);
				  f.add(b1);
				  f.add(b2);
				  f.add(b3);
			      f.setLayout(null);  
				  f.setVisible(true);

	}

}
