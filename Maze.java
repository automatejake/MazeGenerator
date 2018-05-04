import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

/**
This is a maze GUI application that draws a maze using recursive backtracking and 
then solves it
**/

public class Maze extends JApplet
{
   //default contructor is 100 cells in a 10 x 10 grid
   public Maze(){
      cell = new Cells[10][10];
   }
   //constructor that accepts inputted size
   public  Maze(int size){
      cell = new Cells[size][size];
   }
   
   private Cells[][] cell; //cell array that holds information about each cell of maze
   private int cx = 0;
   private int cy = 0;
   private final int N = 0;
   private final int E = 1;
   private final int S = 2;
   private final int W = 3;
   private ArrayList<Integer> availableDirs; //temporarily created to display possible options
   //these variable act the same as an enumerated class and are for solving the maze
   private final int DEFAULT = 0; //all cells initialized as default  
   private final int CORRECT = 1; //marks this if it is part of correct path
   private final int POSSIBLE = 2; //marks this if it is possibly correct path
   private final int FORK = 3; //marks this if it is a fork with multiple choices
   private final int WRONG = 4; //marks this if it is not part of the correct path
     
   public class Cells{
      boolean[] walls; //array to hold N E S W walls of each cell
      boolean visited; //tells maze generator whether or not the cell has been visited
      boolean revisited; //tells maze generator whether or not the cell has been revisited
      
      int marked; //marked is true if the cell is part of the solution path
      
      public Cells(){
         walls = new boolean[4];
         walls[N]=true;
         walls[E]=true;
         walls[S]=true;
         walls[W]=true;
         visited = false;
         revisited = false;
         marked = DEFAULT;
      }
   }
   
   /**This method builds the maze**/
   public void buildMaze()
   {
      //initializes new cell array
      for(int i = 0; i < cell.length; i++){
         for(int j = 0; j < cell.length; j++){
            cell[i][j] = new Cells();
         }
      }
      
      Random rand = new Random();
      
      //clears walls
      for(int i = 0; i < cell.length*cell.length*2-2; i++)
      {
         availableDirs = new ArrayList<>();
         
         //checks if North is available
         if(cy != 0 && cell[cy-1][cx].visited == false){
            availableDirs.add(N);
         }
         //checks if East is available
         if(cx != cell.length-1 && cell[cy][cx+1].visited == false){
            availableDirs.add(E);
         }
         //checks if South is available
         if(cy != cell.length-1 && cell[cy+1][cx].visited == false){
            availableDirs.add(S);
         }       
         //checks if West is available
         if(cx != 0 && cell[cy][cx-1].visited == false){
            availableDirs.add(W);
         }
         
         if(availableDirs.size() != 0){//only executes if there is a wall available to carve
         //sets current cell visisted to true, sets wall of given direction false, changes coordinates, sets new wall of opposite direction false 
            switch(availableDirs.get(rand.nextInt(availableDirs.size()))){ //determines next direction
               case N: cell[cy][cx].visited = true; cell[cy][cx].walls[N] = false; cy -= 1; cell[cy][cx].walls[S] = false; 
                  break;
               case E: cell[cy][cx].visited = true; cell[cy][cx].walls[E] = false; cx += 1; cell[cy][cx].walls[W] = false;
                  break;
               case S: cell[cy][cx].visited = true; cell[cy][cx].walls[S] = false; cy += 1; cell[cy][cx].walls[N] = false;
                  break;
               case W: cell[cy][cx].visited = true; cell[cy][cx].walls[W] = false; cx -= 1; cell[cy][cx].walls[E] = false;
                  break;
            }
         }
         else{ //retraces to a connected wall that is already carved   
            //checks if North is available
            if(cell[cy][cx].walls[N] == false && cell[cy-1][cx].revisited == false){
               cell[cy][cx].visited = true;
               cell[cy][cx].revisited = true;
               cy-=1; 
            }
            //checks if East is available
            else if(cell[cy][cx].walls[E] == false && cell[cy][cx+1].revisited == false){
               cell[cy][cx].visited = true;
               cell[cy][cx].revisited = true;
               cx += 1;
            }
            //checks if South is available
            else if( cell[cy][cx].walls[S] == false && cell[cy+1][cx].revisited == false){
               cell[cy][cx].visited = true;
               cell[cy][cx].revisited = true;
               cy += 1;
            }       
            //checks if West is available
            else if(cell[cy][cx].walls[W] == false && cell[cy][cx-1].revisited == false){
               cell[cy][cx].visited = true;
               cell[cy][cx].revisited = true;
               cx -= 1;
            }
            else{System.out.println(availableDirs.size());}
         }  
      }
   }
   
    /**This method solves the maze and marks the path taken to solve it**/
   public void solveMaze()
   {
      /**
      move until reached 1st fork, marking blocks with 1 for correct path
      mark fork with 3
         mark following cells with 2 as possible
      if all options exhausted, mark all 2s as 0s and return to most recent 3
      if fork has no remaining options, mark 0 and go back to most recent 3
      
      finally in graphics, mark all 1s and 3s as red  
      **/
      
      cx = 0; 
      cy = 0;
      boolean temp = false; //true when reached fork to mark possible
      
      int endPoint = cell.length-1;
      
      while(cx != endPoint || cy != endPoint){
      //for(int i = 0; i < 100; i++){
           
         availableDirs = new ArrayList<>();
            //checks if East is available
         if(cell[cy][cx].walls[E] == false && cell[cy][cx+1].marked == DEFAULT){
            availableDirs.add(E);
         }
            //checks if South is available
         if( cell[cy][cx].walls[S] == false && cell[cy+1][cx].marked == DEFAULT){
            availableDirs.add(S);
         }       
            //checks if West is available
         if(cell[cy][cx].walls[W] == false && cell[cy][cx-1].marked == DEFAULT){
            availableDirs.add(W);
         }
                   //checks if North is available
         if(cell[cy][cx].walls[N] == false && cell[cy-1][cx].marked == DEFAULT){
            availableDirs.add(N); 
         }  
         
         if(availableDirs.size() > 1)
         {
            cell[cy][cx].marked = FORK;
            switch(availableDirs.get(0)){
               case N: cy -= 1;
                  break;
               case E: cx += 1;
                  break;
               case S: cy += 1;
                  break;
               case W: cx -= 1;
                  break;
            }
            temp = true;
         }
         else if(availableDirs.size() == 1 && temp == true)//changes pos for after fork
         {
            cell[cy][cx].marked = POSSIBLE;
            switch(availableDirs.get(0)){
               case N: cy -= 1;
                  break;
               case E: cx += 1;
                  break;
               case S: cy += 1;
                  break;
               case W: cx -= 1;
                  break;
            }
         }
         else if(availableDirs.size() == 1 && temp == false)
         {
            cell[cy][cx].marked = CORRECT;
            switch(availableDirs.get(0)){
               case N: cy -= 1;
                  break;
               case E: cx += 1;
                  break;
               case S: cy += 1;
                  break;
               case W: cx -= 1;
                  break;
            }
         }
         else //if all positions are exhausted
         {
            cell[cy][cx].marked = WRONG;
            //checks if East is available
            if(cell[cy][cx].walls[E] == false && cell[cy][cx+1].marked != WRONG){
               cx += 1;
               temp = false;
            }
            //checks if South is available
            else if( cell[cy][cx].walls[S] == false && cell[cy+1][cx].marked != WRONG){
               cy += 1;
               temp = false;
            }       
            //checks if West is available
            else if(cell[cy][cx].walls[W] == false && cell[cy][cx-1].marked != WRONG){
               cx -= 1;
               temp = false;
            }
                   //checks if North is available
            else if(cell[cy][cx].walls[N] == false && cell[cy-1][cx].marked != WRONG){
               cy -= 1;
               temp = false; 
            }
         }
         //cell[cy][cx].marked = CORRECT;
      }
   }

   
   /**This method draws the maze by constructing rectangles that represent the
   NSEW walls of each cell of the maze**/
   public void paint(Graphics g){
      int xPos = 0;
      int yPos = 0;
      int size = 400/cell.length-2;
      
      
      buildMaze();
      solveMaze();
     
      for(int i = 0; i < cell.length; i++){
         for(int j = 0; j < cell.length; j++){
            g.setColor(Color.blue);
            if(cell[i][j].walls[N] == true){  //Draws North Walls
               g.fillRect(xPos,yPos,size,2);
            }
            if(i != cell.length-1 || j != cell.length-1){ //Leaves maze end wall open
               if(cell[i][j].walls[E] == true){  //Draws East Walls
                  g.fillRect(xPos+size,yPos,2,size);
               }
            }
            if(cell[i][j].walls[S] == true){  //Draws South Walls
               g.fillRect(xPos,yPos+size,size,2);
            }
            if(i != 0 || j != 0){ //Leaves maze start wall open
               if(cell[i][j].walls[W] == true){//Draws West Walls
                  g.fillRect(xPos,yPos,2,size);
               }
            }
            if(cell[i][j].marked == CORRECT || cell[i][j].marked == FORK || cell[i][j].marked == POSSIBLE || 
            (i == cell.length-1 && j == cell.length-1))//Draws the correct path
            {
               g.setColor(Color.red);
               g.fillOval(xPos+size/4,yPos+size/4,size/2,size/2);
            }
            xPos += size;
         }
         xPos = 0;
         yPos += size; 
      }
          
   }
}
