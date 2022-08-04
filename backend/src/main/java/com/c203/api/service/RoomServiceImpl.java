package com.c203.api.service;

import com.c203.api.dto.Room.RoomCreateDto;
import com.c203.api.dto.Room.RoomDecoDto;
import com.c203.api.dto.Room.RoomModifyDto;
import com.c203.api.dto.Room.RoomShowDto;
import com.c203.db.Entity.Room;
import com.c203.db.Entity.RoomDeco;
import com.c203.db.Entity.User;
import com.c203.db.Repository.RoomDecoRepository;
import com.c203.db.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomDecoRepository roomDecoRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Override
    public RoomDecoDto createRoom(RoomCreateDto roomCreateDto) throws Exception {
        Room room = new Room();
        RoomDeco roomDeco = new RoomDeco();
        roomDeco.setRoomdeco_bg(roomCreateDto.getPartyBg());
        roomDeco.setRoomdeco_candle(roomCreateDto.getPartyCandle());
        roomDeco.setRoomdeco_object(roomCreateDto.getPartyCake());
        room.setRoomDesc(roomCreateDto.getPartyDesc());
        room.setRoom_date(roomCreateDto.getPartyDate());
        room.setRoom_opendate(LocalDateTime.now());
        room.setRoomHost(roomCreateDto.getPartyHost());
        room.setRoomName(roomCreateDto.getPartyName());
        // 저장
        roomRepository.save(room);
        roomDecoRepository.save(roomDeco);
        // 리턴
        RoomDecoDto roomDecoDto = new RoomDecoDto();
        roomDecoDto.setDate(room.getRoom_date());
        roomDecoDto.setBg(roomDeco.getRoomdeco_bg());
        roomDecoDto.setCandle(roomDeco.getRoomdeco_candle());
        roomDecoDto.setObject(roomDeco.getRoomdeco_object());
        // 암호화 하기
        int num = room.getRoomIdx();
        String n = Integer.toString(num);
        String temp = encryptionService.encrypt(n);
        roomDecoDto.setRoomId(temp); // 프론트에 암호화한 room_idx던져주기
        return roomDecoDto;
    }

    @Override
    @Transactional
    public boolean deleteRoom(String email, String idx) throws Exception {
        // 암호화된 room_idx로 원래 값 찾기
        String temp = encryptionService.decrypt(idx);
        int id = Integer.parseInt(temp);
        // email이랑 idx값 같으면 room정보 삭제
        roomRepository.deleteByRoomIdxAndRoomHost(id, email);
        // idx값 같은 roomdeco정보 삭제
        roomDecoRepository.deleteByRoomdecoIdx(id);
        return true;
    }

    @Override
    public boolean modifyRoom(RoomModifyDto roomModifyDto, String roomIdx) throws Exception {
        // 원래 room_idx 원래 값
        String temp = encryptionService.decrypt(roomIdx);
        int id = Integer.parseInt(temp);
        // 기존 방, 기존 데코 값 가져오기
        Room room = roomRepository.findByRoomIdxAndRoomHost(id, roomModifyDto.getPartyHost());
        room.setRoom_date(roomModifyDto.getPartyDate());
        room.setRoomName(roomModifyDto.getPartyName());
        room.setRoomDesc(roomModifyDto.getPartyDesc());
        RoomDeco roomDeco = roomDecoRepository.findByRoomdecoIdx(id);
        roomDeco.setRoomdeco_bg(roomModifyDto.getPartyBg());
        roomDeco.setRoomdeco_candle(roomModifyDto.getPartyCandle());
        roomDeco.setRoomdeco_object(roomModifyDto.getPartyCake());
        // 다시 저장
        roomRepository.save(room);
        roomDecoRepository.save(roomDeco);
        return true;
    }

    @Override
    public RoomShowDto showRoom(String email, String idx) throws Exception {// 원래 room_idx 원래 값
        String temp = encryptionService.decrypt(idx);
        int id = Integer.parseInt(temp);
        Room room = roomRepository.findByRoomIdxAndRoomHost(id,email);
        RoomDeco roomDeco = roomDecoRepository.findByRoomdecoIdx(id);
        if(room != null && roomDeco != null){
            RoomShowDto roomShowDto = new RoomShowDto();
            roomShowDto.setPartyBg(roomDeco.getRoomdeco_bg());
            roomShowDto.setPartyCake(roomDeco.getRoomdeco_object());
            roomShowDto.setPartyCandle(roomDeco.getRoomdeco_candle());
            roomShowDto.setPartyDate(room.getRoom_date());
            roomShowDto.setPartyDesc(room.getRoomDesc());
            roomShowDto.setPartyName(room.getRoomName());
            return roomShowDto;
        }
        else return null;
    }

}