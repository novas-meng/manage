package com.example.tsbx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
public class UserOperateService {
	private XMPPConnection conncetion = (XMPPConnection) ClientConServer.connection;
	
	private AccountManager accountmanger = conncetion.getAccountManager();
	
	private Roster roster = conncetion.getRoster();
	
	public boolean regAccount(String _username, String _password, Map<String,String> attributes){
		boolean regmsg = false;
		try {
			accountmanger.createAccount(_username, _password, attributes);
			regmsg = true;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return regmsg;
	}
	
	public boolean addGroup(String _groupname){
		try{
			roster.createGroup(_groupname);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	
	}
	
	
	public List<RosterGroup> getAllGroup(){
		List<RosterGroup> grouplist = new ArrayList<RosterGroup>();  
        Collection<RosterGroup> rosterGroup = roster.getGroups();  
        Iterator<RosterGroup> i = rosterGroup.iterator();  
        while (i.hasNext()) {
            grouplist.add(i.next());  
        }  
        return grouplist;  

	}
	
	public List<Object> getAllGroupName(){
		List<RosterGroup> grouplist = this.getAllGroup();
		List<Object> list = new ArrayList<Object>();
		for(int i = 0; i < grouplist.size(); i++){
			list.add(grouplist.get(i).getName());
		}
		return list;
	}

	public boolean addNewFriend(String _friendJIDname, String _friendNickName){
		try {
			roster.createEntry(_friendJIDname, _friendNickName, null);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addNewFriend(String _friendJIDname, String _friendNickName, String _groupName){
		try {
			roster.createEntry("v@michael-pc", "v", new String[] {"婵����锟斤拷"});
//			roster.createEntry(_friendJIDname, _friendNickName, new String[] {_groupName});
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteFriend(String _userName){
		
		try {
			
			RosterEntry rosterEntity = roster.getEntry(_userName);
			roster.removeEntry(rosterEntity);
			return true;
		}catch(XMPPException e){
			e.printStackTrace();
			return false;
		}
	}
}
