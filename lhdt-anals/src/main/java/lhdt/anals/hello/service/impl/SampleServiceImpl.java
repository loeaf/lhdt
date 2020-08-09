package lhdt.anals.hello.service.impl;

import lhdt.anals.hello.domain.SubType0;
import lhdt.anals.hello.persistence.SampleRepository;
import lhdt.anals.hello.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SampleServiceImpl extends SampleService {
    @Autowired
    SampleRepository studyRepository;

    @Override
    public SubType0 save(SubType0 vo) {
        return this.studyRepository.save(vo);
    }

    @Override
    public List<SubType0> findAll() {
        ArrayList<SubType0> result = new ArrayList<SubType0>();
        this.studyRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public SubType0 findById(Long id) {
        return this.studyRepository.findById(id).orElse(null);
    }

    @Override
    public List<SubType0> findAllById(Long id) {
        ArrayList<SubType0> result = new ArrayList<SubType0>();
        this.studyRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public boolean existVoByUk(SubType0 vo) {
        return this.studyRepository.existsByHelloName(vo.getHelloName());
    }

    @Override
    public SubType0 findByUk(SubType0 vo) {
        return this.studyRepository.findByHelloName(vo.getHelloName());
    }

    @Override
    public List<SubType0> findAllByUk(SubType0 vo) {
        return this.studyRepository.findAllByHelloName(vo.getHelloName());
    }

    @Override
    public void deleteByVo(SubType0 vo) {
        this.studyRepository.delete(vo);
    }
}
