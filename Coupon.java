package nexus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;

public class Coupon extends PluginBase{
  private Config config;
  List<String> playerList=new ArrayList<String>();
  
  @Override
  public void onEnable(){
    this.getDataFolder().mkdirs();
    this.config=new Config(getDataFolder()+"/config.json",Config.JSON);
  }
  
  @Override
  public void onDisable(){
    this.config.save();
  }
  


  public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    if(command.getName().equals("쿠폰")){
      if(!(sender instanceof Player)){
        sender.sendMessage("§c[쿠폰] 플레이어만 사용 가능한 명령어 입니다.");
        return true;
      }
      Player player=(Player)sender;
      if(args.length==0){
      	 return false;
      }
      if(args[0].equals("입력")||args[0].equalsIgnoreCase("insert")||args[0].equalsIgnoreCase("i")){
      	 if(args.length<2){
      	 	  player.sendMessage("§7[쿠폰] /쿠폰 <입력> <코드>");
      	 	  return true;
      	 }
      	 if(!this.config.exists(args[1])){
      		player.sendMessage("§e[쿠폰] 존재하지 않는 쿠폰입니다.");
      		return true;
      	 }
      	 playerList=this.config.getStringList(args[1]);
      	 double reward;
      	 if(this.isUser(player.getName(),playerList)){
      	 	  player.sendMessage("§e[쿠폰] 이미 사용한 쿠폰입니다.");
      	 	  return true;
      	 }
      	 if(!this.config.isDouble(args[1])){
      		 reward=this.config.getInt(args[1]+"-reward");
      	 }
      	 else{
      		 reward=this.config.getDouble(args[1]+"-reward");
      	 }
      	 playerList.add(player.getName());
      	 this.config.set(args[1],playerList);
      	 EconomyAPI.getInstance().addMoney(player,reward);
      	 player.sendMessage("§3[쿠폰] 쿠폰을 입력하였습니다. +"+reward);
      	 this.getServer().getLogger().info(player.getName()+"-"+args[1]);
      	 return true;
      }
      if(args[0].equals("생성")||args[0].equalsIgnoreCase("create")||args[0].equalsIgnoreCase("c")){
      	 if(args.length<3){
      	 	  player.sendMessage("§7[쿠폰] /쿠폰 <생성> <코드> <보상 금액>");
      	 	  return true;
      	 }
      	 if(!player.isOp()){
      	 	  player.sendMessage("§4[쿠폰] 오피만 사용 가능한 명령어 입니다.");
      	 	  return true;
      	 }
      	 if(this.config.exists(args[1])){
      	 	  player.sendMessage("§e[쿠폰] 이미 존재하는 쿠폰입니다.");
      	 	  return true;
      	 }
      	 Pattern pattern=Pattern.compile("^[0-9]*$");
      	 Matcher matcher=pattern.matcher(args[2]);
      	 if(!matcher.find()){
      		player.sendMessage("§e[쿠폰] 숫자로 입력해주세요.");
      		return true;
      	 }
      	 this.config.set(args[1]+"-reward",args[2]);
      	 this.config.set(args[1],new ArrayList<String>());
      	 player.sendMessage("§3[쿠폰] 쿠폰이 생성되었습니다.");
      	 return true;
      }
      if(args[0].equals("삭제")||args[0].equalsIgnoreCase("delete")||args[0].equalsIgnoreCase("d")){
      	 if(args.length<2){
      	 	  player.sendMessage("§4[쿠폰] /쿠폰 <삭제> <코드>");
      	 	  return true;
      	 }
      	 if(!player.isOp()){
      	 	  player.sendMessage("§e[쿠폰] 오피만 사용 가능한 명령어입니다.");
      	 	  return true;
      	 }
      	 if(!this.config.exists(args[1])){
      	 	  player.sendMessage("§7[쿠폰] 존재하지 않는 쿠폰입니다.");
      	 	  return true;
      	 }
      	 this.config.remove(args[1]);
      	 this.config.remove(args[1]+"-reward");
      	 player.sendMessage("§3[쿠폰] 쿠폰을 삭제하였습니다.");
      	 return true;
      }
    }
    return true;
  }
  public boolean isUser(String name,List<String> playerList){
  	 if(playerList.contains(name)){
  	 	  return true;
  	 }
  	 return false;
  }
}