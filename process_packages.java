package DS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Request {
    public Request(int arrival_time, int process_time) {
        this.arrival_time = arrival_time;
        this.process_time = process_time;
    }

    public int arrival_time;
    public int process_time;
}

class Response {
    public Response(boolean dropped, int start_time) {
        this.dropped = dropped;
        this.start_time = start_time;
    }

    public boolean dropped;
    public int start_time;
}

class Buffer {
    public Buffer(int size) {
        this.size_ = size;
        this.finish_time_ = new ArrayList<Integer>();
    }

    public Response Process(Request request) {
        // write your code here
        int finish_temp=(request.arrival_time+request.process_time);
        if(finish_time_.size()==0){
        	finish_time_.add(finish_temp);
        	return new Response(false, request.arrival_time);
        }
        else{
        	for(int i=0;i<finish_time_.size();i++){
        		if(finish_time_.get(i)<=request.arrival_time){
        			finish_time_.remove(i);
        		}else 
        			break;
        	}
        	if(finish_time_.size()<size_){
        		if(finish_time_.size()==0){
                	finish_time_.add(finish_temp);
                	return new Response(false, request.arrival_time);
                }else
        		if(finish_time_.get(finish_time_.size()-1)>request.arrival_time){
        			request.arrival_time=finish_time_.get(finish_time_.size()-1);
        		}
        		finish_temp=(request.arrival_time+request.process_time);
        		finish_time_.add(finish_temp);
        		return new Response(false, request.arrival_time);
        	}else{
        		return new Response(true, request.arrival_time);
        	}
        	/*if(finish_time_.get(0)<=request.arrival_time){
        		finish_time_.remove(0);
        		finish_time_.add(finish_temp);
        		return new Response(false, request.arrival_time);
        	}else{
        		if(finish_time_.size()<size_){
        			finish_time_.add(finish_temp);
        			return new Response(false, request.arrival_time);
        		}else{
        			return new Response(true, request.arrival_time);
        		}
        	}*/
        }
        // return new Response(false, -1);
    }

    private int size_;
    private ArrayList<Integer> finish_time_;
}

class process_packages {
    private static ArrayList<Request> ReadQueries(Scanner scanner) throws IOException {
        int requests_count = scanner.nextInt();
        //constraint defined
        if(requests_count<1 || requests_count>Math.pow(10,5)){
        	System.exit(0);
        }
        ArrayList<Request> requests = new ArrayList<Request>();
        for (int i = 0; i < requests_count; ++i) {
            int arrival_time = scanner.nextInt();
            int process_time = scanner.nextInt();
            requests.add(new Request(arrival_time, process_time));
        }

        //constraint defined
        for(int i = 0; i < requests_count; ++i){
        	Request request1=requests.get(i);
        	if(request1.arrival_time<0 || request1.arrival_time>Math.pow(10,5) || request1.process_time<0 || request1.process_time>Math.pow(10,3)){
        		System.exit(0);        		
        	}
        }

        //constraint defined
        for(int i = 0; i < requests_count-1; ++i){
        	Request request1=requests.get(i);
        	Request request2=requests.get(i+1);
        	if(request1.arrival_time>request2.arrival_time){
        		System.exit(0);
        	}
        }
        return requests;
    }

    private static ArrayList<Response> ProcessRequests(ArrayList<Request> requests, Buffer buffer) {
        ArrayList<Response> responses = new ArrayList<Response>();
        for (int i = 0; i < requests.size(); ++i) {
            responses.add(buffer.Process(requests.get(i)));
        }
        return responses;
    }

    private static void PrintResponses(ArrayList<Response> responses) {
        for (int i = 0; i < responses.size(); ++i) {
            Response response = responses.get(i);
            if (response.dropped) {
                System.out.println(-1);
            } else {
                System.out.println(response.start_time);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int buffer_max_size = scanner.nextInt();
        //constraint defined
        if(buffer_max_size<1 || buffer_max_size>Math.pow(10,5)){
        	System.exit(0);
        }
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        ArrayList<Response> responses = ProcessRequests(requests, buffer);
        PrintResponses(responses);
    }
}
