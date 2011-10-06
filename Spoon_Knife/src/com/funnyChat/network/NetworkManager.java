package com.funnyChat.network;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;

public class NetworkManager {
	private int mIdCount;
	private static NetworkManager mInstance;
	private HashMap<Integer, Connection> mConnections;
	private int mTimeout;
	private int mPingInterval;
	private int mMaxCount;
	
	private NetworkManager(int _max_count){
		mIdCount = 0;
		mConnections = new HashMap<Integer, Connection>();
		mTimeout = 5000;
		mPingInterval = 5000;
		mMaxCount = _max_count;
	}
	
	public static boolean initialize(int _max_count){
		if(mInstance == null){
			mInstance = new NetworkManager(_max_count);
			return true;
		}
		return false;
	}
	
	public static boolean initialize(){
		return initialize(50);
	}
	
	public boolean deinitialize(){
		if(mInstance != null){
			removeAll();
			mConnections = null;
			mInstance = null;
			return true;
		}
		return false;
	}
	
	public static NetworkManager getInstance(){
		return mInstance;
	}
	
	public int getTimeout(){
		return mTimeout;
	}
	
	public void setTimeout(int _timeout){
		mTimeout = _timeout;
	}
	
	public int getPingInterval(){
		return mPingInterval;
	}
	
	public void setPingInterval(int _interval){
		mPingInterval = _interval;
	}
	
	public int getMaxCount(){
		return mMaxCount;
	}
	
	public void setMaxCount(int _max_count){
		mMaxCount = _max_count;
	}
	
	protected int generateId(){
		return mIdCount++;
	}
	
	public void removeAll(){
		mConnections.clear();
	}
	
	public boolean disconnect(int _id){
		try{
			Connection _connection = mConnections.get(_id);
			if(_connection != null){
				mConnections.remove(_id);
				_connection.getSocketChannel().close();
				
				return true;
			}
			else{
				return false;
			}
		}
		catch(IOException e){
			//Wait for logger...
			return false;
		}
	}
	
	public int connect(InetAddress ip, int port){
		try{
			SocketAddress _address = new InetSocketAddress(ip, port);
			SocketChannel _socket_channel = SocketChannel.open(_address);
			_socket_channel.configureBlocking(false);
			Connection _connection = new Connection(_socket_channel);
			//
			//Send hello message.
			//
			int _id = generateId();
			mConnections.put(_id, _connection);
			return _id;
		}
		catch(IOException e){
			//Wait for logger...
			return -1;
		}
	}
	
	public void send(Package _data){
		
	}
}
