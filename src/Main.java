import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    final static int[][] seats = {{3,2},{4,3},{2,3},{3,4}};
    final static int queue = 30;
    public static void main(String[] args) {
        seating(seats,queue);
    }
    private static void seating(int[][] seats,int queue){
        //initializing values
        int[] seatSum = {0,0,0}; //aisle,window,centre
        ArrayList<int[][]> seatViews = new ArrayList<>();
        int partitionCounter = 0;
        for(int[] partition : seats){
            seatSum[2] += partition[1] * Math.max((partition[0]-2),0); //centre
            if(partitionCounter == 0 || partitionCounter == seats.length-1){
                seatSum[1] += partition[1]; //window
                seatSum[0] += partition[1]; //aisle
            }else{
                seatSum[0] += partition[1] * Math.min(partition[0],2); //aisle
            }
            int[][] partitionFill = new int[partition[1]][partition[0]];
            for(int[] row:partitionFill)
                Arrays.fill(row,0);
            seatViews.add(partitionFill);
            partitionCounter++;
        }
        //actual seating of passenger part
        int[] seatsCounters = {1,seatSum[0]+1,seatSum[0]+seatSum[1]+1};//aisle,window,centre
        partitionCounter = 0;
        int counter = 0;
        int row = 0;
        while(true){
            int[][]view =seatViews.get(partitionCounter);
            if(view.length > row){
                for(int col = 0; col<view[1].length; col++){
                    boolean b = seatsCounters[1] > seatSum[0] + seatSum[1];
                    if(partitionCounter == 0 && col == 0){
                        //windows left side
                        if(b) continue;
                        view[row][col] = seatsCounters[1];
                        seatsCounters[1] = seatsCounters[1] + 1;
                    }else if(partitionCounter == seats.length -1 && col == view[1].length-1){
                        //windows right side
                        if(b) continue;
                        view[row][col] = seatsCounters[1];
                        seatsCounters[1] = seatsCounters[1] + 1;
                    }else if(col == 0 || col == view[1].length-1){
                        //aisles
                        if(seatsCounters[0] > seatSum[0]) continue;
                        view[row][col] = seatsCounters[0];
                        seatsCounters[0] = seatsCounters[0] + 1;
                    }else {
                        //centre
                        if(seatsCounters[2] > queue) continue;
                        view[row][col] = seatsCounters[2];
                        seatsCounters[2] = seatsCounters[2] + 1;
                    }
                }
            }
            seatViews.set(partitionCounter,view);
            counter++;
            partitionCounter++;
            if(counter >= queue) break;
            if(partitionCounter == seats.length) {
                partitionCounter = 0;
                row++;
            }
        }
        //printing
        System.out.println(seatViews.stream().map(x -> Arrays.deepToString(x).replace("],", "],\n")).collect(Collectors.joining("\n")));
    }

}
