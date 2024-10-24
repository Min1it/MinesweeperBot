package org.example;

public interface MinesweeperBotBrain {
    //checks numbered squares to see if # of -1's(unaccounted squares) == # of the squares, then identifies the square(s)
    //if performance ever becomes a problem a quick(er) fix would be to roughly track where the moves are being made, and limit the search to that area,
    //  or just keep track of the edges somehow idk
    public static MinesweeperBotEyes simpleFindMine(MinesweeperBotEyes board){
        for (int x=0; x<board.board.length;x++){
            for(int y=0; y<board.board[0].length;y++){
                int numberOfUnknowns = board.getNumberOfAround(-1, x, y);
                if ((numberOfUnknowns>0) && (numberOfUnknowns == (board.board[x][y]) - board.getNumberOfAround(9, x, y)) && (board.board[x][y] >=1) && (board.board[x][y] <= 8)){
                    if(board.getNW(x,y) == -1){
                        board.board[x-1][y-1] = 9;
                    }
                    if(board.getN(x,y) == -1){
                        board.board[x][y-1] = 9;
                    }
                    if(board.getNE(x,y) == -1){
                        board.board[x+1][y-1] = 9;
                    }
                    if(board.getW(x,y) == -1){
                        board.board[x-1][y] = 9;
                    }
                    if(board.getE(x,y) == -1){
                        board.board[x+1][y] = 9;
                    }
                    if(board.getSW(x,y) == -1){
                        board.board[x-1][y+1] = 9;
                    }
                    if(board.getS(x,y) == -1){
                        board.board[x][y+1] = 9;
                    }
                    if(board.getSE(x,y) == -1){
                        board.board[x+1][y+1] = 9;
                    }
                }
            }
        }
        return board;
    }

    //doing this weird modular thing in this way is making the program stupid inefficient, but whatever. So is separating the simpleFindMine and simpleFindZero, since they're the same thing
    //ok this doesn't find Zero's, it just find clear spaces, so when we do more advanced stuff, we need to rerun it to update those values
   public static MinesweeperBotEyes simpleFindZero(MinesweeperBotEyes board){
       for (int x=0; x<board.board.length;x++) {
           for (int y = 0; y < board.board[0].length; y++) {
                if((board.getNumberOfAround(9, x, y) == (board.board[x][y]) && (board.board[x][y] >= 1) && (board.board[x][y] <= 8))){
                    if(board.getNW(x,y) == -1){
                        board.board[x-1][y-1] = 0;
                    }
                    if(board.getN(x,y) == -1){
                        board.board[x][y-1] = 0;
                    }
                    if(board.getNE(x,y) == -1){
                        board.board[x+1][y-1] = 0;
                    }
                    if(board.getW(x,y) == -1){
                        board.board[x-1][y] = 0;
                    }
                    if(board.getE(x,y) == -1){
                        board.board[x+1][y] = 0;
                    }
                    if(board.getSW(x,y) == -1){
                        board.board[x-1][y+1] = 0;
                    }
                    if(board.getS(x,y) == -1){
                        board.board[x][y+1] = 0;
                    }
                    if(board.getSE(x,y) == -1){
                        board.board[x+1][y+1] = 0;
                    }
                }
           }
       }
       return board;
   }
    //Inspired by Apple Maths video on youtube
   public static MinesweeperBotEyes comparePairUsingSets(MinesweeperBotEyes board){
       for (int x=0; x<board.board.length - 1;x++) {
           for (int y = 0; y < board.board[0].length - 1; y++) {
                if (board.is1Through8(x, y)){
                    if(board.is1Through8(x+1, y)){
                        int xOffset = 1;
                        int yOffset = 0;

                        int value1 = board.board[x][y] - board.getNumberOfAround(9,x,y);
                        int value2 = board.board[x+xOffset][y+yOffset] - board.getNumberOfAround(9,x+xOffset,y+yOffset);

                        int unknownsExclusive1 = 0;
                        for (int x1 = 0; x1<3;x1++) {
                            for (int y1 = 0; y1 < 3; y1++) {
                                if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                    continue;
                                }
                                if((Math.abs((-1+x1)-(xOffset)) == 2) ||(Math.abs((-1+y1)-(yOffset)) == 2)){
                                    if(board.board[x-1+x1][y-1+y1] == -1) {
                                        unknownsExclusive1++;
                                    }
                                }
                            }
                        }

                        if((value1-value2)==unknownsExclusive1){
                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)-(xOffset)) == 2) || (Math.abs((-1+y1)-(yOffset)) == 2)){
                                       if(board.board[x-1+x1][y-1+y1] == -1){
                                           board.board[x-1+x1][y-1+y1] = 9;
                                       }
                                    }
                                }
                            }

                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)+(xOffset)) == 2) || (Math.abs((-1+y1)+(yOffset)) == 2)){
                                        if(board.board[x-1+x1+xOffset][y-1+yOffset+y1] == -1){
                                            board.board[x-1+x1+xOffset][y-1+yOffset+y1] = 0;
                                        }
                                    }
                                }
                            }
                        }

                        int unknownsExclusive2 = 0;
                        for (int x1 = 0; x1<3;x1++) {
                            for (int y1 = 0; y1 < 3; y1++) {
                                if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                    continue;
                                }
                                if((Math.abs((-1+x1)+(xOffset)) == 2) ||(Math.abs((-1+y1)+(yOffset)) == 2)){
                                    if(board.board[x-1+x1+xOffset][y-1+y1+yOffset] == -1) {
                                        unknownsExclusive2++;
                                    }
                                }
                            }
                        }

                        if((value2-value1)==unknownsExclusive2){
                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)-(xOffset)) == 2) || (Math.abs((-1+y1)-(yOffset)) == 2)){
                                        if(board.board[x-1+x1][y-1+y1] == -1){
                                            board.board[x-1+x1][y-1+y1] = 0;
                                        }
                                    }
                                }
                            }

                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)+(xOffset)) == 2) || (Math.abs((-1+y1)+(yOffset)) == 2)){
                                        if(board.board[x-1+x1+xOffset][y-1+yOffset+y1] == -1){
                                            board.board[x-1+x1+xOffset][y-1+yOffset+y1] = 9;
                                        }
                                    }
                                }
                            }
                        }


                    }


                //the next 2 (x+1 y+1) and (x+0 y+1) are untested
               if(board.is1Through8(x+1, y+1)){
                   int xOffset = 1;
                   int yOffset = 1;

                   int value1 = board.board[x][y] - board.getNumberOfAround(9,x,y);
                   int value2 = board.board[x+xOffset][y+yOffset] - board.getNumberOfAround(9,x+xOffset,y+yOffset);

                   int unknownsExclusive1 = 0;
                   for (int x1 = 0; x1<3;x1++) {
                       for (int y1 = 0; y1 < 3; y1++) {
                           if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                               continue;
                           }
                           if((Math.abs((-1+x1)-(xOffset)) == 2) ||(Math.abs((-1+y1)-(yOffset)) == 2)){
                               if(board.board[x-1+x1][y-1+y1] == -1) {
                                   unknownsExclusive1++;
                               }
                           }
                       }
                   }

                   if((value1-value2)==unknownsExclusive1){
                       for (int x1=0; x1<3;x1++) {
                           for (int y1 = 0; y1 < 3; y1++) {
                               if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                   continue;
                               }
                               if((Math.abs((-1+x1)-(xOffset)) == 2) || (Math.abs((-1+y1)-(yOffset)) == 2)){
                                   if(board.board[x-1+x1][y-1+y1] == -1){
                                       board.board[x-1+x1][y-1+y1] = 9;
                                   }
                               }
                           }
                       }

                       for (int x1=0; x1<3;x1++) {
                           for (int y1 = 0; y1 < 3; y1++) {
                               if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                   continue;
                               }
                               if((Math.abs((-1+x1)+(xOffset)) == 2) || (Math.abs((-1+y1)+(yOffset)) == 2)){
                                   if(board.board[x-1+x1+xOffset][y-1+y1+yOffset] == -1){
                                       board.board[x-1+x1+xOffset][y-1+y1+yOffset] = 0;
                                   }
                               }
                           }
                       }
                   }

                   int unknownsExclusive2 = 0;
                   for (int x1 = 0; x1<3;x1++) {
                       for (int y1 = 0; y1 < 3; y1++) {
                           if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                               continue;
                           }
                           if((Math.abs((-1+x1)+(xOffset)) == 2) ||(Math.abs((-1+y1)+(yOffset)) == 2)){
                               if(board.board[x-1+x1+xOffset][y-1+y1+yOffset] == -1) {
                                   unknownsExclusive2++;
                               }
                           }
                       }
                   }

                   if((value2-value1)==unknownsExclusive2){
                       for (int x1=0; x1<3;x1++) {
                           for (int y1 = 0; y1 < 3; y1++) {
                               if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                   continue;
                               }
                               if((Math.abs((-1+x1)-(xOffset)) == 2) || (Math.abs((-1+y1)-(yOffset)) == 2)){
                                   if(board.board[x-1+x1][y-1+y1] == -1){
                                       board.board[x-1+x1][y-1+y1] = 0;
                                   }
                               }
                           }
                       }

                       for (int x1=0; x1<3;x1++) {
                           for (int y1 = 0; y1 < 3; y1++) {
                               if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                   continue;
                               }
                               if((Math.abs((-1+x1)+(xOffset)) == 2) || (Math.abs((-1+y1)+(yOffset)) == 2)){
                                   if(board.board[x-1+x1+xOffset][y-1+yOffset+y1] == -1){
                                       board.board[x-1+x1+xOffset][y-1+yOffset+y1] = 9;
                                   }
                               }
                           }
                       }
                   }


               }

                    if(board.is1Through8(x, y+1)){
                        int xOffset = 0;
                        int yOffset = 1;

                        int value1 = board.board[x][y] - board.getNumberOfAround(9,x,y);
                        int value2 = board.board[x+xOffset][y+yOffset] - board.getNumberOfAround(9,x+xOffset,y+yOffset);

                        int unknownsExclusive1 = 0;
                        for (int x1 = 0; x1<3;x1++) {
                            for (int y1 = 0; y1 < 3; y1++) {
                                if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                    continue;
                                }
                                if((Math.abs((-1+x1)-(xOffset)) == 2) ||(Math.abs((-1+y1)-(yOffset)) == 2)){
                                    if(board.board[x-1+x1][y-1+y1] == -1) {
                                        unknownsExclusive1++;
                                    }
                                }
                            }
                        }

                        if((value1-value2)==unknownsExclusive1){
                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)-(xOffset)) == 2) || (Math.abs((-1+y1)-(yOffset)) == 2)){
                                        if(board.board[x-1+x1][y-1+y1] == -1){
                                            board.board[x-1+x1][y-1+y1] = 9;
                                        }
                                    }
                                }
                            }

                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)+(xOffset)) == 2) || (Math.abs((-1+y1)+(yOffset)) == 2)){
                                        if(board.board[x-1+x1+xOffset][y-1+yOffset+y1] == -1){
                                            board.board[x-1+x1+xOffset][y-1+yOffset+y1] = 0;
                                        }
                                    }
                                }
                            }
                        }

                        int unknownsExclusive2 = 0;
                        for (int x1 = 0; x1<3;x1++) {
                            for (int y1 = 0; y1 < 3; y1++) {
                                if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                    continue;
                                }
                                if((Math.abs((-1+x1)+(xOffset)) == 2) ||(Math.abs((-1+y1)+(yOffset)) == 2)){
                                    if(board.board[x-1+x1+xOffset][y-1+y1+yOffset] == -1) {
                                        unknownsExclusive2++;
                                    }
                                }
                            }
                        }

                        if((value2-value1)==unknownsExclusive2){
                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1==-1)||(y-1+y1==-1)||((x1==1)&&(y1==1))){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)-(xOffset)) == 2) || (Math.abs((-1+y1)-(yOffset)) == 2)){
                                        if(board.board[x-1+x1][y-1+y1] == -1){
                                            board.board[x-1+x1][y-1+y1] = 0;
                                        }
                                    }
                                }
                            }

                            for (int x1=0; x1<3;x1++) {
                                for (int y1 = 0; y1 < 3; y1++) {
                                    if((x-1+x1+xOffset==-1)||(y-1+y1+yOffset==-1)||((x1==1)&&(y1==1))||(x-1+x1+xOffset==board.board.length)||(y-1+y1+yOffset==board.board[0].length)){
                                        continue;
                                    }
                                    if((Math.abs((-1+x1)+(xOffset)) == 2) || (Math.abs((-1+y1)+(yOffset)) == 2)){
                                        if(board.board[x-1+x1+xOffset][y-1+yOffset+y1] == -1){
                                            board.board[x-1+x1+xOffset][y-1+yOffset+y1] = 9;
                                        }
                                    }
                                }
                            }
                        }


                    }

                }
           }
       }
       return board;

   }
//depthLimit should be high, around 30 at least, maybe
    //recursion is hard
   public static MinesweeperBotEyes backtrackingRecursion(MinesweeperBotEyes board, int depth, int depthLimit){
        if (board.isSuccess == true){
            return board;
        }
        if(board.isCompleteFailure==true){
            System.out.println("complete failure");
            return board;
        }
        //alternative and smarter way to transmirt failure up when needed, isntead of just depth
        if (depth>=depthLimit){
            board.isCompleteFailure = true;
            return board;
        }
        boolean isSuccess = true;
       for(int x1=0; x1<board.board.length; x1++){
           for(int y1=0; y1<board.board[0].length; y1++){
               if((board.board[x1][y1] >= 1) && (board.board[x1][y1] <= 8)){
                   if(board.getNumberOfAround(9, x1, y1)!=board.board[x1][y1]){
                       isSuccess = false;
                   }
               }
           }
       }
       if(isSuccess){
           System.out.println("SUCCESS DETECTED");
          board.isSuccess = true;
          return board;
       }


       for(int x = 0; x<board.board.length; x++){
          for(int y=0; y<board.board[0].length; y++){
              if(board.board[x][y] == -1){
                  boolean isEdge = false;

                  if((board.getNW(x, y) >= 1) && (board.getNW(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getN(x, y) >= 1) && (board.getN(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getNE(x, y) >= 1) && (board.getNE(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getW(x, y) >= 1) && (board.getW(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getE(x, y) >= 1) && (board.getE(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getSW(x, y) >= 1) && (board.getSW(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getS(x, y) >= 1) && (board.getS(x,y)<=8)){
                      isEdge = true;
                  }
                  if((board.getSE(x, y) >= 1) && (board.getSE(x,y)<=8)){
                      isEdge = true;
                  }

                  if(isEdge){
                      boolean isValid = true;
                      board.board[x][y] = 9;
                      if((board.getNW(x,y)!= -2) && (board.getNW(x,y)>=1) && (board.getNW(x,y)<=8) && (board.getNW(x,y)<board.getNumberOfAround(9,x-1, y-1))){
                          isValid = false;
                      }
                      if((board.getN(x, y) != -2) && (board.getN(x,y)>=1) && (board.getN(x,y)<=8) && (board.getN(x,y)<board.getNumberOfAround(9, x, y-1))){
                          isValid = false;
                      }
                      if((board.getNE(x, y) != -2) && (board.getNE(x,y)>=1) && (board.getNE(x,y)<=8) && (board.getNE(x,y)<board.getNumberOfAround(9, x+1, y-1))){
                          isValid = false;
                      }
                      if((board.getW(x, y) != -2) && (board.getW(x,y)>=1) && (board.getW(x,y)<=8) && (board.getW(x,y)<board.getNumberOfAround(9, x-1, y))){
                          isValid = false;
                      }
                      if((board.getE(x, y) != -2) && (board.getE(x,y)>=1) && (board.getE(x,y)<=8) && (board.getE(x,y)< board.getNumberOfAround(9, x+1, y))){
                          isValid = false;
                      }
                      if((board.getSW(x, y) != -2) && (board.getSW(x,y)>=1) && (board.getSW(x,y)<=8) && (board.getSW(x,y)< board.getNumberOfAround(9, x-1, y+1))){
                          isValid = false;
                      }
                      if((board.getS(x, y) != -2) && (board.getS(x,y)>=1) && (board.getS(x,y)<=8) && (board.getS(x,y)< board.getNumberOfAround(9, x, y+1))){
                          isValid = false;
                      }
                      if((board.getSE(x, y) != -2) && (board.getSE(x,y)>=1) && (board.getSE(x,y)<=8) && (board.getSE(x,y)< board.getNumberOfAround(9, x+1, y+1))){
                          isValid = false;
                      }
                      if(!isValid){
                          board.board[x][y] = -1;
                      }

                      if(isValid){

                          MinesweeperBotEyes value = backtrackingRecursion(board, depth+1, depthLimit);
                          if(value.isSuccess){
                              return value;
                          }
                          if(( value.isCompleteFailure)||(value.isPartialFailure)){
                              board.board[x][y] = -1;
                              continue;
                          }
                      }
                  }



                  //who cares about efficiency :p


              }
          }
       }
       board.isPartialFailure = true;
       return board;

   }



   //Smartest way to do this will probably to find all probabilities, then the edges, then follow path from one probability to another and hope only one solution comes out.




}
