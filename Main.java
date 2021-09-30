import java.util.*;
class Shark {
  int x, y, id, dir;
  int[][] turnPriority = new int[4][4];
  boolean isAlive = true;
  public Shark(){};
  public Shark(int x, int y, int id, int dir){
    this.x = x;
    this.y = y;
    this.id = id;
    this.dir = dir;
  };
}
class Position {
  int x;
  int y;
  int id;
  int time;
  boolean isShark = false;
  ArrayList<Shark> sharks = new ArrayList<>();

  public Position(){};
  Position(int x, int y, int id, int time){
    this.x = x;
    this.y = y;
    this.id = id;
    this.time = time;
  };
}
class Main {
  public static int n,m,k;
  public static Position[][] graph = new Position[20][20];
  public static ArrayList<Shark> sharkList = new ArrayList<>();
  public static int[][] vector = {{-1,0},{0,1},{0,-1},{0,1}}; 
  public static int resultTime = 0;
  public static void sharkMove(Shark shark){
    if(!shark.isAlive)
      return;
    int x = shark.x;
    int y = shark.y;
    int dir = shark.dir;
    int id = shark.id;
    ArrayList<Shark> temp = new ArrayList<>();
    for(int i=0; i<4; i++){
      int nx = x + vector[i][0];
      int ny = y + vector[i][1];
      if(nx < 0 || nx >= n || ny < 0 || ny >= n)
        continue;
      if(graph[nx][ny].time == 0){
        temp.add(new Shark(nx, ny, id, i));
      }
    }
    if(temp.size() == 0){
      for(int i=0; i<4; i++){
        int nx = x + vector[i][0];
        int ny = y + vector[i][1];
        if(nx < 0 || nx >= n || ny < 0 || ny >= n)
          continue;
        if(graph[nx][ny].time != 0 && graph[nx][ny].id == id){
          temp.add(new Shark(nx, ny, id, i));
        }
      }
    }
    if(temp.size() > 1){
      int[] priority = shark.turnPriority[dir]; 
      Collections.sort(temp, new Comparator<Shark>(){
        @Override
        public int compare(Shark p1, Shark p2){
          int d1 = p1.dir;
          int d2 = p2.dir;
          int idx1 = 0;
          int idx2 = 0;
          for(int i=0; i<priority.length; i++){
            if(priority[i] == d1)
              idx1 = i;
            else if(priority[i] == d2)
              idx2 = i;
          }
          return idx1 - idx2;
        }
      });
    }
    Shark target = temp.get(0);
    int newX = target.x;
    int newY = target.y; 
    System.out.println(x + " " + y + "to" + newX +  " " + newY);
    Position pos = graph[newX][newY];
    pos.sharks.add(shark);

    if(pos.isShark){
      if(graph[newX][newY].id >= id){
        shark.isAlive = false;
      }else{
        sharkList.get(pos.id-1).isAlive = false;
        shark.x = newX;
        shark.y = newY;
        shark.dir = target.dir;
        pos.id = id;
        pos.time = k;
      }
    }else{
      pos.id = id;
      pos.time = k;
      shark.x = newX;
      shark.y = newY;
      pos.isShark = true;
    }
    graph[x][y].isShark = false;
  }
  public static void process(){
    while(resultTime <= 1000){
      for(int i=0; i<sharkList.size(); i++){
        sharkMove(sharkList.get(i));
      }
      for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
          if(graph[i][j].sharks.size() > 1){
            Collections.sort(graph[i][j].sharks, new Comparator<Shark>(){
              @Override
              public int compare(Shark s1, Shark s2){
                return s1.id - s2.id;
              }
            });
            Shark selected = graph[i][j].sharks.get(0);
            Position pos = graph[i][j];
            pos.id = selected.id;
            pos.dir = selected.dir;
            for(int k=1; k<graph[i][j].size(); k++){
              
            }
            pos.sharks = new ArrayList<Shark>();
          }
          
        }
      }
      for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
          if(graph[i][j].time > 0){
            graph[i][j].time -= 1;
          }
        }
      }
      int cnt = 0;
      boolean flag = true;
      for(int i=0; i<sharkList.size(); i++){
        if(sharkList.get(i).isAlive)
          cnt++;
        if(cnt > 1)
          flag = false;
      }
      if(flag){
        return;
      }
      resultTime++;
    }
    resultTime = -1;
    return;
  }
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    
    n = sc.nextInt();
    m = sc.nextInt();
    k = sc.nextInt();
    
    for(int i=0; i<n; i++){
      for(int j=0; j<n; j++){
        int id = sc.nextInt();
        Position pos = new Position();
        pos.x = i;
        pos.y = j;
        pos.id = id;
        if(id != 0){
          Shark shark = new Shark();
          shark.x = i;
          shark.y = j;
          shark.id = id;
          pos.time = k;
          pos.isShark = true;
          sharkList.add(shark);
        }
        graph[i][j] = pos;
      }
    }
    Collections.sort(sharkList, new Comparator<Shark>(){
      @Override
      public int compare(Shark s1, Shark s2){
        return s1.id - s2.id;
      }
    });
    for(int i=0; i<sharkList.size(); i++){
      int dir = sc.nextInt();
      sharkList.get(i).dir = dir-1;
    }

    for(int i=0; i<sharkList.size(); i++){
      int[][] temp = new int[4][4];
      for(int j=0; j<4; j++){
        for(int k=0; k<4; k++){
          temp[j][k] = sc.nextInt() - 1;
        }
      }
      sharkList.get(i).turnPriority = temp;
    }
    process();
    System.out.println(resultTime);
  }
}