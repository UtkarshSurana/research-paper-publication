
package com.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.PaperRepository;
import com.app.dao.TopicRepository;
import com.app.dao.UserRepository;
import com.app.dto.PaperRegistrationDTO;
import com.app.dto.PaperRespDTO;
import com.app.entities.Paper;
import com.app.entities.Topic;
import com.app.entities.User;

@Service
@Transactional
public class PaperServiceImpl implements IPaperService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PaperRepository paperRepo;

	@Autowired
	private TopicRepository topicRepo;	

	@Autowired
	private ModelMapper mapper;

	@Value("${file.upload.location}")
	private String folderLocation;
	
	@Value("${pdfFile.upload.location}")
	private String folderLocation2;

	@PostConstruct
	public void anyInit() {
		File folder = new File(folderLocation);
		if (!folder.exists())
			folder.mkdirs();
		
		File folder2 = new File(folderLocation2);
		if (!folder2.exists())
			folder2.mkdirs();
	}

	@Override
	public String addPaper(long userId, long topicId, PaperRegistrationDTO paperDto)
			throws IOException {

		Paper paper = mapper.map(paperDto, Paper.class);

		Topic topic = topicRepo.findById(topicId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid topic id!!!"));

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid user id!!!"));

		String path = folderLocation + File.separator + paperDto.getImgFile().getOriginalFilename();
		String path2 = folderLocation2 + File.separator + paperDto.getPdfFile().getOriginalFilename();

		paper.setAuthor(user);
		paper.setTopic(topic);
		paper.setImage(path);
		paper.setPdfData(path2);
		paperRepo.save(paper);

		Files.copy(paperDto.getImgFile().getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(paperDto.getPdfFile().getInputStream(), Paths.get(path2), StandardCopyOption.REPLACE_EXISTING);

		return "Submitted successfully with id " + paper.getId();
	}

	@Override
	public byte[] restoreImage(long paperId) throws IOException {
		Paper paper = paperRepo.findById(paperId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid paper ID : Can't save image !!!!!!!"));
		String path = paper.getImage();
		if (path != null)
			return Files.readAllBytes(Paths.get(path));
		throw new ResourceNotFoundException("Image not  yet assigned , for author " + paper.getId());
	}
	
	@Override
	public byte[] restorePdf(long paperId) throws IOException {
		Paper paper = paperRepo.findById(paperId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid paper ID : Can't save image !!!!!!!"));
	
		String path = paper.getPdfData();
		if (path != null) {
			paper.setVisits(paper.getVisits()+1);
			return Files.readAllBytes(Paths.get(path));
			}
		throw new ResourceNotFoundException("PDF not  yet assigned , for emp " + paper.getId());
	}

	@Override
	public List<PaperRespDTO> getPapersByTopicId(long topicId) throws IOException {
		
		 List<PaperRespDTO> list=paperRepo.findByStatusTrueAndTopicId(topicId).stream().map(pap -> mapper.map(pap, PaperRespDTO.class))
				.collect(Collectors.toList());
		 list.forEach(p->{
			try {
				p.setImg(restoreImage(p.getId()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return list;
	}

	@Override
	public List<PaperRespDTO> getPapersByAuthorId(long authorId) throws IOException {
		
		return paperRepo.findByAuthorId(authorId).stream().map(pap -> mapper.map(pap, PaperRespDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<PaperRespDTO> getPapersByStatus() throws IOException {
		
		return paperRepo.findByStatusFalse().stream().map(pap -> mapper.map(pap, PaperRespDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public String partialUpdateStatus(long paperId,boolean status) {
		Paper paper = paperRepo.findById(paperId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid paper ID !!!!!!!"));
		if(status==true)
			paper.setStatus(true);
		return " Paper details updated";
	}
	
	@Override
	public String deletePaper(long paperId) {		
		paperRepo.deleteById(paperId);
		return "deleted paper details with id " + paperId;
	}
}