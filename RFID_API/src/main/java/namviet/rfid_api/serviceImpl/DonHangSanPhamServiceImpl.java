package namviet.rfid_api.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.DonHang;
import namviet.rfid_api.entity.DonHangSanPham;
import namviet.rfid_api.entity.Dulieu;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.*;
import namviet.rfid_api.repository.*;
import namviet.rfid_api.service.DonHangSanPhamService;
import namviet.rfid_api.utils.ConvertToHex;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DonHangSanPhamServiceImpl implements DonHangSanPhamService {

    final DonHangSanPhamRepository donHangSanPhamRepository;
    final DonHangSanPhamMapper donHangSanPhamMapper;
    final DonHangRepository donHangRepository;
    final SanPhamMapper sanPhamMapper;
    final DonHangMapper donHangMapper;
    final ThietBiMapper thietBiMapper;
    final NhanVienMapper nhanVienMapper;
    final UpcRepository upcRepository;
    final SanPhamRepository sanPhamRepository;
    final DuLieuRepository duLieuRepository;

    @Override
    public List<DonHangSanPhamDTO> dSSanPhamTheoDonHang(String maLenh) {
        List<DonHangSanPham> dSDonHangSP = donHangSanPhamRepository.findByDonHangMaLenhContaining(maLenh);
        return dSDonHangSP.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonHangSanPhamDTO> dSTatCaSanPham() {
        List<DonHangSanPham> dSTatCaSP = donHangSanPhamRepository.findAll();
        return dSTatCaSP.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonHangSanPhamDTO capNhatDonHangSanPham(DonHangSanPhamDTO donHangSanPhamDTO) {
        if (donHangSanPhamDTO == null || donHangSanPhamDTO.getDonHangSanPhamId() == null) {
            throw new IllegalArgumentException("Dữ liệu đơn hàng hoặc ID không hợp lệ");
        }
        Optional<DonHangSanPham> optionalDonHang = donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamDTO.getDonHangSanPhamId());
        if (optionalDonHang.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng với ID: " + donHangSanPhamDTO.getDonHangSanPhamId());
        }
        DonHangSanPham donHangSanPham = optionalDonHang.get();
        DonHang donHang = donHangSanPham.getDonHang();

        donHangSanPham.setSanPham(sanPhamMapper.toEntity(donHangSanPhamDTO.getSanPham()));
        donHangSanPham.setSoLuong(donHangSanPhamDTO.getSoLuong());
        donHangSanPham.setDonHang(donHangMapper.toEntity(donHangSanPhamDTO.getDonHang()));

        SanPhamDTO sanPhamDTO = sanPhamMapper.toDto(donHangSanPham.getSanPham());
        donHangSanPham.setTenFile(taoTenFile(sanPhamDTO));

        donHangSanPham = donHangSanPhamRepository.save(donHangSanPham);

        return donHangSanPhamMapper.toDTO(donHangSanPham);
    }

    @Override
    @Transactional
    public List<DonHangSanPhamDTO> themSPVaoDonHangNew(List<DonHangSanPhamDTO> dsDonHangSanPhamDTO) {
        List<DonHangSanPhamDTO> result = new ArrayList<>();
        DonHang donHang = donHangRepository.findByDonHangId(dsDonHangSanPhamDTO.get(0).getDonHang().getDonHangId());
        if (donHang == null) {
            return null;
        }

        for (DonHangSanPhamDTO donHangSanPhamDTO : dsDonHangSanPhamDTO) {
            SanPham sanPham = sanPhamMapper.toEntity(donHangSanPhamDTO.getSanPham());

            DonHangSanPham donHangSanPham = new DonHangSanPham();
            donHangSanPham.setDonHang(donHang);
            donHangSanPham.setSanPham(sanPham);
            donHangSanPham.setTenFile(taoTenFile(sanPhamMapper.toDto(sanPham)));
            donHangSanPham.setSoLuong(donHangSanPhamDTO.getSoLuong());

            donHangSanPhamRepository.save(donHangSanPham);

            DonHangSanPhamDTO savedDonHangSanPhamDTO = donHangSanPhamMapper.toDTO(donHangSanPham);
            result.add(savedDonHangSanPhamDTO);
        }

        return result;
    }

    @Override
    public Optional<DonHangSanPhamDTO> timDonHangSanPham(int donHangSanPhamId) {
        if(donHangSanPhamId <= 0){
            return Optional.empty();
        }
        return donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamId)
                .map(donHangSanPhamMapper::toDTO);
    }

    @Override
    public Optional<List<DonHangSanPhamDTO>> timDonHangTheoDonHangId(int donHangId) {
        if(donHangId <= 0){
            return Optional.empty();
        }
        List<DonHangSanPham> dsDonHangDonHangId = donHangSanPhamRepository.findByDonHangDonHangId(donHangId);
        if(dsDonHangDonHangId.isEmpty()){
            return Optional.empty();
        }
        List<DonHangSanPhamDTO> dsDonHangSanPhamDTO = dsDonHangDonHangId.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(dsDonHangSanPhamDTO);
    }


    private String taoTenFile(SanPhamDTO sanPham) {
        return taoTenFile(sanPham, 0);
    }

    private String taoTenFile(SanPhamDTO sanPham, int index) {
        if (sanPham == null || sanPham.getSku() == null) {
            throw new IllegalArgumentException("SanPham or SKU cannot be null");
        }

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

        String sku = sanPham.getSku();
        String ngayThang = dateFormat.format(date);
        String thoiGian = timeFormat.format(date);

        String baseFileName = sku + "-" + ngayThang + "-" + thoiGian;
        if (index > 0) {
            baseFileName += " (" + index + ")";
        }
        return baseFileName + ".xlsx";
    }
    private List<Dulieu> listDuLieu ;

    @Override
    @Transactional
    public List<DonHangSanPhamDTO> importFile(List<MultipartFile> dsFileImport, String maLenh, String sku, int viTriEPC, boolean isHex) {
        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        SanPham sanPham = sanPhamRepository.findBySku(sku);

        if (donHang == null) {
            throw new CustomException("Don Hang not found", HttpStatus.BAD_REQUEST);
        }
        if (sanPham == null) {
            throw new CustomException("SKU not found", HttpStatus.BAD_REQUEST);
        }

        Set<String> existingFiles = ConcurrentHashMap.newKeySet();
        existingFiles.addAll(donHangSanPhamRepository.findAllTenFile());

        List<DonHangSanPham> dsDonHangSanPham = Collections.synchronizedList(new ArrayList<>());
        List<Dulieu> allDuLieu = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        dsFileImport.forEach(multipartFile -> {
            executorService.submit(() -> {
                try {
                    DonHangSanPham donHangSanPham = readFileExcelExportDonHangSanPham(multipartFile, donHang, sanPham, viTriEPC, isHex);

                    if (existingFiles.contains(donHangSanPham.getTenFile())) {
                        throw new CustomException("File already exists: " + donHangSanPham.getTenFile(), HttpStatus.BAD_REQUEST);
                    }
                    existingFiles.add(donHangSanPham.getTenFile());

                    donHangSanPham.setSoLanTao(1);
                    dsDonHangSanPham.add(donHangSanPham);

                    listDuLieu.forEach(d -> d.setDonHangSanPham(donHangSanPham));
                    allDuLieu.addAll(listDuLieu);
                    listDuLieu.clear();

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CustomException("Error processing file: " + multipartFile.getOriginalFilename(), HttpStatus.BAD_REQUEST);
                }
            });
        });

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new CustomException("Error waiting for file processing to complete", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Save in larger batches
        final int batchSize = 10000;
        for (int i = 0; i < dsDonHangSanPham.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dsDonHangSanPham.size());
            donHangSanPhamRepository.saveAll(dsDonHangSanPham.subList(i, end));
        }
        for (int i = 0; i < allDuLieu.size(); i += batchSize) {
            int end = Math.min(i + batchSize, allDuLieu.size());
            duLieuRepository.saveAll(allDuLieu.subList(i, end));
        }

        return dsDonHangSanPham.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
    @Override
    @Transactional
    public List<DonHangSanPhamDTO> importFile(List<MultipartFile> dsFileImport, String maLenh, String sku, int viTriEPC, boolean isHex) {
        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        SanPham sanPham = sanPhamRepository.findBySku(sku);
        if(donHang == null){
            throw new CustomException("Don Hang not found",HttpStatus.BAD_REQUEST);
        }
        if(sanPham == null){
            throw new CustomException("SKU not found", HttpStatus.BAD_REQUEST);
        }
        List<DonHangSanPham> dsDonHangSanPham = new ArrayList<>();

        for (MultipartFile multipartFile : dsFileImport) {
            try {
                DonHangSanPham donHangSanPham = readFileExcelExportDonHangSanPham(multipartFile, donHang, sanPham,viTriEPC,isHex);

                boolean exists = donHangSanPhamRepository.existsByTenFile(donHangSanPham.getTenFile());
                if (exists) {
                    throw new CustomException("File with the same name already exists: " + donHangSanPham.getTenFile(),HttpStatus.BAD_REQUEST);
                }
                donHangSanPham.setSoLanTao(1);

                DonHangSanPham savedDhSp =  donHangSanPhamRepository.save(donHangSanPham);
                dsDonHangSanPham.add(savedDhSp);

                for(Dulieu dulieu: listDuLieu) {
                    dulieu.setDonHangSanPham(savedDhSp);
                }
                duLieuRepository.saveAll(listDuLieu);
                listDuLieu.clear();
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace( );
                throw new CustomException("Error saving DonHangSanPham: Duplicate key violation for file: " + multipartFile.getOriginalFilename(), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error processing file: " + multipartFile.getOriginalFilename() + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        return dsDonHangSanPham.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }*/
/**
    @Override
    @Transactional
    public List<DonHangSanPhamDTO> importFile(List<MultipartFile> dsFileImport, String maLenh, String sku, int viTriEPC, boolean isHex) {
        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        SanPham sanPham = sanPhamRepository.findBySku(sku);

        if (donHang == null) {
            throw new CustomException("Don Hang not found", HttpStatus.BAD_REQUEST);
        }
        if (sanPham == null) {
            throw new CustomException("SKU not found", HttpStatus.BAD_REQUEST);
        }

        Set<String> existingFiles = new HashSet<>(donHangSanPhamRepository.findAllTenFile()); // Lấy trước danh sách file

        List<DonHangSanPham> dsDonHangSanPham = new ArrayList<>();
        List<Dulieu> allDuLieu = new ArrayList<>();

        for (MultipartFile multipartFile : dsFileImport) {
            try {
                DonHangSanPham donHangSanPham = readFileExcelExportDonHangSanPham(multipartFile, donHang, sanPham, viTriEPC, isHex);

                if (existingFiles.contains(donHangSanPham.getTenFile())) {
                    throw new CustomException("File already exists: " + donHangSanPham.getTenFile(), HttpStatus.BAD_REQUEST);
                }

                donHangSanPham.setSoLanTao(1);
                dsDonHangSanPham.add(donHangSanPham);
                existingFiles.add(donHangSanPham.getTenFile());

                // Cập nhật DonHangSanPham cho Dulieu
                listDuLieu.forEach(d -> d.setDonHangSanPham(donHangSanPham));
                allDuLieu.addAll(listDuLieu);
                listDuLieu.clear();

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error processing file: " + multipartFile.getOriginalFilename(), HttpStatus.BAD_REQUEST);
            }
        }

        // Lưu hàng loạt để tối ưu tốc độ
        donHangSanPhamRepository.saveAll(dsDonHangSanPham);
        duLieuRepository.saveAll(allDuLieu);

        return dsDonHangSanPham.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }*/


    @Override
    @Transactional
    public List<DonHangSanPhamDTO> themSPVaoDonHangFile(MultipartFile multipartFile, String maLenh) {
        if (maLenh == null || maLenh.isEmpty()) {
            throw new CustomException("Mã lệnh không được để trống", HttpStatus.BAD_REQUEST);
        }

        String fileName = multipartFile.getOriginalFilename();
        boolean isXlsx = fileName != null && fileName.endsWith(".xlsx");
        boolean isXls = fileName != null && fileName.endsWith(".xls");

        if (!isXls && !isXlsx) {
            throw new CustomException("File không đúng định dạng (.xls,.xlsx)", HttpStatus.BAD_REQUEST);
        }

        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        if (donHang == null) {
            throw new CustomException("Không tìm thấy đơn hàng với mã lệnh: " + maLenh, HttpStatus.BAD_REQUEST);
        }

        List<DonHangSanPham> donHangSanPhams = new ArrayList<>();
        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell skuCell = row.getCell(1);
                Cell quantityCell = row.getCell(2);

                if (skuCell == null || quantityCell == null) {
                    throw new CustomException("Dữ liệu không hợp lệ tại dòng: " + (i + 1), HttpStatus.BAD_REQUEST);
                }

                String sku = skuCell.getStringCellValue();
                SanPham sanPham = sanPhamRepository.findBySku(sku);
                if (sanPham == null) {
                    throw new CustomException("Không tìm thấy sản phẩm với SKU: " + sku, HttpStatus.BAD_REQUEST);
                }

                int soLuong = (int) quantityCell.getNumericCellValue();
                if (soLuong <= 0) {
                    throw new CustomException("Số lượng không hợp lệ tại dòng: " + (i + 1), HttpStatus.BAD_REQUEST);
                }

                DonHangSanPham donHangSanPham = new DonHangSanPham();
                donHangSanPham.setDonHang(donHang);
                donHangSanPham.setSanPham(sanPham);
                donHangSanPham.setSoLuong(soLuong);
                String tenFile = taoTenFile(sanPhamMapper.toDto(sanPham));

                int index = 1;
                while (donHangSanPhamRepository.existsByTenFile(tenFile)) {
                    tenFile = taoTenFile(sanPhamMapper.toDto(sanPham), index);
                    index++;
                }
                donHangSanPham.setTenFile(tenFile);
                donHangSanPhams.add(donHangSanPham);
                donHangSanPhamRepository.save(donHangSanPham);
            }



        } catch (IOException e) {
            throw new CustomException("Lỗi khi đọc file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException("Lỗi không xác định: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return donHangSanPhams.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonHangSanPhamDTO themMotSanPhamVaoDonHang(String sku, String maLenh, int soLuong) {
        if (maLenh == null || maLenh.isEmpty()) {
            throw new CustomException("Mã lệnh không được để trống", HttpStatus.BAD_REQUEST);
        }
        SanPham sanPham = sanPhamRepository.findBySku(sku);
        if (sku == null || sanPham == null) {
            throw new CustomException("Sản phẩm không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        DonHang donHang = donHangRepository.findByMaLenh(maLenh);
        if (donHang == null) {
            throw new CustomException("Không tìm thấy đơn hàng với mã lệnh: " + maLenh, HttpStatus.BAD_REQUEST);
        }



        DonHangSanPham donHangSanPham = new DonHangSanPham();
        donHangSanPham.setDonHang(donHang);
        donHangSanPham.setSanPham(sanPham);
        donHangSanPham.setSoLuong(soLuong);
        String tenFile = taoTenFile(sanPhamMapper.toDto(sanPham));
        donHangSanPham.setTenFile(tenFile);
        return donHangSanPhamMapper.toDTO(donHangSanPhamRepository.save(donHangSanPham));
    }

    private DonHangSanPham readFileExcelExportDonHangSanPham(MultipartFile multipartFile, DonHang donHang, SanPham sanPham, int viTriEPC, boolean isHex) {
        listDuLieu = new ArrayList<>();
        String fileName = multipartFile.getOriginalFilename();
        boolean isXlsx = fileName != null && fileName.endsWith(".xlsx");
        boolean isXls = fileName != null && fileName.endsWith(".xls");

        if (!isXls && !isXlsx) {
            throw new CustomException("File không đúng định dạng (.xls,.xlsx)", HttpStatus.BAD_REQUEST);
        }
        DonHangSanPham donHangSanPham;
        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int soLuong = sheet.getLastRowNum();

            donHangSanPham = new DonHangSanPham();
            donHangSanPham.setTenFile(fileName);
            donHangSanPham.setSoLuong(soLuong);
            donHangSanPham.setDonHang(donHang);
            donHangSanPham.setSanPham(sanPham);

            for(int i = 1; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                if(row == null) continue;

                Dulieu dulieu = new Dulieu();
                StringBuilder sb = new StringBuilder();

                for (int j = 0; j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null && j != viTriEPC) {
                        sb.append(getCellStringValue(cell)).append("|");
                    }
                    if (j == viTriEPC) {
                        Cell cellEPC = row.getCell(viTriEPC);
                        if (cellEPC != null) {
                            dulieu.setDataGoc(getCellStringValue(cellEPC));
                            if(isHex){
                                dulieu.setEpc(ConvertToHex.convertToHexadecimal(getCellStringValue(cellEPC)));
                            } else {
                                dulieu.setEpc(getCellStringValue(cellEPC));
                            }
                        }
                    }
                }

                dulieu.setSku(sanPham.getSku());
                dulieu.setDonHang(donHang);
                dulieu.setSanPham(sanPham);
                dulieu.setNoiDungBienDoi(sb.toString());

                listDuLieu.add(dulieu);
            }

        } catch (Exception e) {
            throw new CustomException("Lỗi khi đọc file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return donHangSanPham;
    }

/**
    private DonHangSanPham readFileExcelExportDonHangSanPham(MultipartFile multipartFile, DonHang donHang, SanPham sanPham, int viTriEPC, boolean isHex) {
        listDuLieu = new ArrayList<>();
        String fileName = multipartFile.getOriginalFilename();
        boolean isXlsx = fileName != null && fileName.endsWith(".xlsx");
        boolean isXls = fileName != null && fileName.endsWith(".xls");

        if (!isXls && !isXlsx) {
            throw new CustomException("File không đúng định dạng (.xls,.xlsx)", HttpStatus.BAD_REQUEST);
        }

        DonHangSanPham donHangSanPham = new DonHangSanPham();
        donHangSanPham.setTenFile(fileName);
        donHangSanPham.setDonHang(donHang);
        donHangSanPham.setSanPham(sanPham);

        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = isXlsx ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum();
            System.out.println("📌 File: " + fileName + " | Tổng số dòng trong sheet: " + totalRows);

            if (totalRows == 0) {
                throw new CustomException("File không có dữ liệu hoặc bị lỗi định dạng", HttpStatus.BAD_REQUEST);
            }

            int soLuong = 0;
            List<Dulieu> batchList = new ArrayList<>();
            final int BATCH_SIZE = 5000;

            // Duyệt từng dòng theo chỉ mục (bỏ qua header - dòng đầu tiên)
            for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    System.out.println("⚠️ Dòng " + rowIndex + " bị null, bỏ qua...");
                    continue;
                }

                Dulieu dulieu = new Dulieu();
                StringBuilder sb = new StringBuilder();
                boolean hasData = false;

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell == null) continue;

                    hasData = true; // Nếu có ít nhất 1 ô có dữ liệu, đánh dấu là dòng hợp lệ
                    if (i != viTriEPC) {
                        sb.append(getCellStringValue(cell)).append("|");
                    } else {
                        String cellValue = getCellStringValue(cell);
                        dulieu.setDataGoc(cellValue);
                        dulieu.setEpc(isHex ? ConvertToHex.convertToHexadecimal(cellValue) : cellValue);
                    }
                }

                if (!hasData) {
                    System.out.println("⚠️ Dòng " + rowIndex + " không có dữ liệu, bỏ qua...");
                    continue;
                }

                dulieu.setSku(sanPham.getSku());
                dulieu.setDonHang(donHang);
                dulieu.setSanPham(sanPham);
                dulieu.setNoiDungBienDoi(sb.toString());

                batchList.add(dulieu);
                soLuong++;

                if (batchList.size() >= BATCH_SIZE) {
                    listDuLieu.addAll(batchList);
                    batchList.clear();
                }
            }

            // Thêm batch còn lại
            if (!batchList.isEmpty()) {
                listDuLieu.addAll(batchList);
            }

            donHangSanPham.setSoLuong(soLuong);
            System.out.println("✅ Đọc xong file: " + fileName + " | Số dòng thực tế: " + soLuong);

        } catch (Exception e) {
            throw new CustomException("Lỗi khi đọc file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return donHangSanPham;
    }
*/

    private String getCellStringValue(Cell cell) {
        if( cell != null){
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "UNKNOWN";
            }
        }
        return "";
    }

}
