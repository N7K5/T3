import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends JFrame {

    int rows= 4, cols= 4;

    int box_size= 40, box_space= 10;

    int win=0, loss=0;

    JButton[][] btns;
    JButton btnReset;

    private JPanel contentPane;
    private JLabel Result_disp;

    boolean delay_pc_input= true;
	Timer timer= new Timer();
	boolean timer_running= false;
    
    static Main frame;
    T3Logic game;

    public static void main(String[] args) {
        run_new_instance();
    }

    public static void run_new_instance() {
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Main();
					frame.setResizable(false);
					frame.setVisible(true);
					frame.setTitle("Tic Tac Toe");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    
    public Main() {

        btns= new JButton[rows][cols];
        game= new T3Logic(rows, cols);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int width= box_size*cols + box_space*(cols+1);
        int height= box_size*rows + box_space*(rows+1)+ (int)(box_size*3);

        setBounds(100, 100, width, height);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
        contentPane.setLayout(null);

        //:  Loop For Main Buttons.....
        for(int row=0; row<rows; row++) {
            for(int col=0; col<cols; col++) {
                btns[row][col]= new JButton("");
                btns[row][col].setBackground(new Color(250, 235, 215));
                btns[row][col].setFont(new Font("Dialog", Font.BOLD, 12));
                btns[row][col].setMargin(new Insets(0, 0, 0, 0) );

                final int r=row, c= col;
                btns[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        btn_clicked(r+" "+c);
                    }
                });
                
                int dx= box_space*(col+1) + box_size*col;
                int dy= box_space*(row+1) + box_size*row;

                btns[row][col].setBounds(dx, dy, box_size, box_size);
                contentPane.add(btns[row][col]);
            }
        }


        //:  Show the result box...
        Result_disp = new JLabel("Result");
		{ 
            int wy= 30;
            int dx= 10;
            int wx= width- 2*dx;
            int dy= rows*box_size + rows*box_space+ box_size/2 - wy/4;
            
            Result_disp.setBounds(dx, dy, wx, wy);
            Result_disp.setHorizontalAlignment(SwingConstants.CENTER);
            Result_disp.setFont(new Font("Dialog", Font.BOLD, 10));
        }
        contentPane.add(Result_disp);


        //:  reset_button setup
        btnReset= new JButton("Reset");
        {
            btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnReset.setToolTipText("Start a new Game...");
            btnReset.setBackground(new Color(255, 182, 193));
            btnReset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    reset_game();
                }
            });
            int dw= box_size*2;
            int dh= box_size*3/4;
            int dx= width/2 - dw/2;
            int dy= height- box_size* 2;
            btnReset.setBounds(dx, dy, dw, dh);
        }
        contentPane.add(btnReset);
    }


    //: When a button is clicked, a string comes to this method...
    void btn_clicked(String _btn) { // _btn is set to "row_index + space + col_index"
        //System.out.println("clicked on"+_btn);
        if(timer_running) {
            set_text("I am thinking", new Color(215, 51, 51));
            return;
        }
        String[] rno= _btn.split(" ");
        boolean did= game.set(Integer.parseInt(rno[0]), Integer.parseInt(rno[1]), "X");
        if(!did) {
            set_text("Wrong Move...");
        }
        else {
            if(!game.isFull()) {
                    timer_running= true;
                    timer.schedule(new TimerTask(){
                        @Override
                        public void run() {
                            
                            try {
                                String pos[]= game.getBestMove("O").split(" ");
                                // System.out.println("Got: " pos[0]+" "+pos[1]+" __");
                                game.set(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), "O");
                            } catch(Exception e) {
                                System.out.println("Exception:\n\t"+e);
                            }
                            refresh();
                            show_score();
                            timer_running= false;
                        }
                    }, 700);
            }
            show_score();
        }
        refresh();
    }

    //: reset the game...
    void reset_game() {
        if(true) {
            game.reset();
            set_text("Reset done...");
        }
        else {
            set_text("Finish this one...");
        }
        
        refresh();
    }

    //: Set a message to the display
    void set_text(String text) {
        Result_disp.setText(text);
    }
    void set_text(String text, Color c) {
        Result_disp.setForeground(c);
        set_text(text);
    }
    void set_text(int win, int loss) {
        set_text("win:"+win+" "+"loss:"+loss, new Color(33, 4, 224));
    }

    void show_score() {
        set_text(win, loss);
    }

    //: to refrest the display
    void refresh() {
        for(int row=0; row<rows; row++) {
            for(int col=0; col<cols; col++) {
                btns[row][col].setText(game.get(row, col));
            }
        }
    }

}