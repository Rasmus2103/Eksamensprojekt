package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.Board;

import java.util.List;

public interface IBoardRepository {
    List<Board> getBoards(int projectid);
    Board getSpecificBoard(int boardid);
    int getBoardIdByProjectId(int projectid);
    int getBacklogBoardIdByProjectId(int projectid);
    int getHistoryBoardIdByProjectId(int projectId);
}
