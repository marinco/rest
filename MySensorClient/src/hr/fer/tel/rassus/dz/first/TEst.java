package hr.fer.tel.rassus.dz.first;

public class TEst {
    public static void main(String[] args){
        String r="{\"ip\":\"localhost\",\"port\":10003}";
        String[] parts=r.split("\"|:|}");
        for(int i=0;i<parts.length;i++){
            System.out.println("Part " + i + "value=" + parts[i]);
        }
    }
}
