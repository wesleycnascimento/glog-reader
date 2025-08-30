package com.example.glogreader.model;

import java.util.ArrayList;
import java.util.List;

public class TcapwsvsRecord {
    private String codLayout; // X(008)
    private int tamLayout;    // 9(005)
    private int scundOrgnz;   // 9(06)
    private String cdigUndOrgnz; // X(01)
    private String iabrevUndOrgnz; // X(40)
    private String agDep; // X(01)
    private int totReg;   // 9(03)
    private List<Ocorrencia> ocorrencias = new ArrayList<>();

    // Getters and Setters
    public String getCodLayout() { return codLayout; }
    public void setCodLayout(String codLayout) { this.codLayout = codLayout; }

    public int getTamLayout() { return tamLayout; }
    public void setTamLayout(int tamLayout) { this.tamLayout = tamLayout; }

    public int getScundOrgnz() { return scundOrgnz; }
    public void setScundOrgnz(int scundOrgnz) { this.scundOrgnz = scundOrgnz; }

    public String getCdigUndOrgnz() { return cdigUndOrgnz; }
    public void setCdigUndOrgnz(String cdigUndOrgnz) { this.cdigUndOrgnz = cdigUndOrgnz; }

    public String getIabrevUndOrgnz() { return iabrevUndOrgnz; }
    public void setIabrevUndOrgnz(String iabrevUndOrgnz) { this.iabrevUndOrgnz = iabrevUndOrgnz; }

    public String getAgDep() { return agDep; }
    public void setAgDep(String agDep) { this.agDep = agDep; }

    public int getTotReg() { return totReg; }
    public void setTotReg(int totReg) { this.totReg = totReg; }

    public List<Ocorrencia> getOcorrencias() { return ocorrencias; }
    public void setOcorrencias(List<Ocorrencia> ocorrencias) { this.ocorrencias = ocorrencias; }

    public static class Ocorrencia {
        private int cprodtServcOper;      // 9(08)
        private int cseriePlanoCaptz;     // 9(004)
        private int ctitloCaptz;          // 9(007)
        private int ctpoPrtcpPsssoa;      // 9(003)
        private int nseqPartcTitlo;       // 9(003)
        private String ctpoPsssoa;        // X(01)
        private long cpsssoa;             // 9(010)
        private long cpsssoaJuridCta;     // 9(010)
        private int ctpoNegocCta;         // 9(003)
        private long nseqNegocCta;        // 9(010)
        private long cpsssoaEnderPartc;   // 9(010)
        private long cemprEnderPartc;     // 9(010)
        private int cseqEnderPartc;       // 9(005)
        private long cpsssoaEmailPartc;   // 9(010)
        private long cpsssoaJuridEmail;   // 9(010)
        private int nseqEmailPartc;       // 9(005)
        private String cindcdPartcCrrtt;  // X(001)
        private double pprtcpResgtTtlar;  // 9(003)V9(002)
        private double pprtcpSortTtlar;   // 9(003)V9(002)
        private int codComlAgCtbil;       // 9(05)
        private long nroCtaBcria;         // 9(13)
        private int digCtaBcria;          // 9(02)
        private int ccpfCnpjTit;          // 9(09)
        private int cdFilialCnpjTit;      // 9(04)
        private int cctrlCpfCnpjTit;      // 9(02)
        private String icliTitul;         // X(40)
        private String elogdrPsssoa;      // X(50)
        private String elogdrNro;         // X(07)
        private String email;             // X(50)
        private int cidtfdGrauParnt;      // 9(01)
        private String rrlctoTtlarSbctr;  // X(30)
		public int getCprodtServcOper() {
			return cprodtServcOper;
		}
		public void setCprodtServcOper(int cprodtServcOper) {
			this.cprodtServcOper = cprodtServcOper;
		}
		public int getCseriePlanoCaptz() {
			return cseriePlanoCaptz;
		}
		public void setCseriePlanoCaptz(int cseriePlanoCaptz) {
			this.cseriePlanoCaptz = cseriePlanoCaptz;
		}
		public int getCtitloCaptz() {
			return ctitloCaptz;
		}
		public void setCtitloCaptz(int ctitloCaptz) {
			this.ctitloCaptz = ctitloCaptz;
		}
		public int getCtpoPrtcpPsssoa() {
			return ctpoPrtcpPsssoa;
		}
		public void setCtpoPrtcpPsssoa(int ctpoPrtcpPsssoa) {
			this.ctpoPrtcpPsssoa = ctpoPrtcpPsssoa;
		}
		public int getNseqPartcTitlo() {
			return nseqPartcTitlo;
		}
		public void setNseqPartcTitlo(int nseqPartcTitlo) {
			this.nseqPartcTitlo = nseqPartcTitlo;
		}
		public String getCtpoPsssoa() {
			return ctpoPsssoa;
		}
		public void setCtpoPsssoa(String ctpoPsssoa) {
			this.ctpoPsssoa = ctpoPsssoa;
		}
		public long getCpsssoa() {
			return cpsssoa;
		}
		public void setCpsssoa(long cpsssoa) {
			this.cpsssoa = cpsssoa;
		}
		public long getCpsssoaJuridCta() {
			return cpsssoaJuridCta;
		}
		public void setCpsssoaJuridCta(long cpsssoaJuridCta) {
			this.cpsssoaJuridCta = cpsssoaJuridCta;
		}
		public int getCtpoNegocCta() {
			return ctpoNegocCta;
		}
		public void setCtpoNegocCta(int ctpoNegocCta) {
			this.ctpoNegocCta = ctpoNegocCta;
		}
		public long getNseqNegocCta() {
			return nseqNegocCta;
		}
		public void setNseqNegocCta(long nseqNegocCta) {
			this.nseqNegocCta = nseqNegocCta;
		}
		public long getCpsssoaEnderPartc() {
			return cpsssoaEnderPartc;
		}
		public void setCpsssoaEnderPartc(long cpsssoaEnderPartc) {
			this.cpsssoaEnderPartc = cpsssoaEnderPartc;
		}
		public long getCemprEnderPartc() {
			return cemprEnderPartc;
		}
		public void setCemprEnderPartc(long cemprEnderPartc) {
			this.cemprEnderPartc = cemprEnderPartc;
		}
		public int getCseqEnderPartc() {
			return cseqEnderPartc;
		}
		public void setCseqEnderPartc(int cseqEnderPartc) {
			this.cseqEnderPartc = cseqEnderPartc;
		}
		public long getCpsssoaEmailPartc() {
			return cpsssoaEmailPartc;
		}
		public void setCpsssoaEmailPartc(long cpsssoaEmailPartc) {
			this.cpsssoaEmailPartc = cpsssoaEmailPartc;
		}
		public long getCpsssoaJuridEmail() {
			return cpsssoaJuridEmail;
		}
		public void setCpsssoaJuridEmail(long cpsssoaJuridEmail) {
			this.cpsssoaJuridEmail = cpsssoaJuridEmail;
		}
		public int getNseqEmailPartc() {
			return nseqEmailPartc;
		}
		public void setNseqEmailPartc(int nseqEmailPartc) {
			this.nseqEmailPartc = nseqEmailPartc;
		}
		public String getCindcdPartcCrrtt() {
			return cindcdPartcCrrtt;
		}
		public void setCindcdPartcCrrtt(String cindcdPartcCrrtt) {
			this.cindcdPartcCrrtt = cindcdPartcCrrtt;
		}
		public double getPprtcpResgtTtlar() {
			return pprtcpResgtTtlar;
		}
		public void setPprtcpResgtTtlar(double pprtcpResgtTtlar) {
			this.pprtcpResgtTtlar = pprtcpResgtTtlar;
		}
		public double getPprtcpSortTtlar() {
			return pprtcpSortTtlar;
		}
		public void setPprtcpSortTtlar(double pprtcpSortTtlar) {
			this.pprtcpSortTtlar = pprtcpSortTtlar;
		}
		public int getCodComlAgCtbil() {
			return codComlAgCtbil;
		}
		public void setCodComlAgCtbil(int codComlAgCtbil) {
			this.codComlAgCtbil = codComlAgCtbil;
		}
		public long getNroCtaBcria() {
			return nroCtaBcria;
		}
		public void setNroCtaBcria(long nroCtaBcria) {
			this.nroCtaBcria = nroCtaBcria;
		}
		public int getDigCtaBcria() {
			return digCtaBcria;
		}
		public void setDigCtaBcria(int digCtaBcria) {
			this.digCtaBcria = digCtaBcria;
		}
		public int getCcpfCnpjTit() {
			return ccpfCnpjTit;
		}
		public void setCcpfCnpjTit(int ccpfCnpjTit) {
			this.ccpfCnpjTit = ccpfCnpjTit;
		}
		public int getCdFilialCnpjTit() {
			return cdFilialCnpjTit;
		}
		public void setCdFilialCnpjTit(int cdFilialCnpjTit) {
			this.cdFilialCnpjTit = cdFilialCnpjTit;
		}
		public int getCctrlCpfCnpjTit() {
			return cctrlCpfCnpjTit;
		}
		public void setCctrlCpfCnpjTit(int cctrlCpfCnpjTit) {
			this.cctrlCpfCnpjTit = cctrlCpfCnpjTit;
		}
		public String getIcliTitul() {
			return icliTitul;
		}
		public void setIcliTitul(String icliTitul) {
			this.icliTitul = icliTitul;
		}
		public String getElogdrPsssoa() {
			return elogdrPsssoa;
		}
		public void setElogdrPsssoa(String elogdrPsssoa) {
			this.elogdrPsssoa = elogdrPsssoa;
		}
		public String getElogdrNro() {
			return elogdrNro;
		}
		public void setElogdrNro(String elogdrNro) {
			this.elogdrNro = elogdrNro;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public int getCidtfdGrauParnt() {
			return cidtfdGrauParnt;
		}
		public void setCidtfdGrauParnt(int cidtfdGrauParnt) {
			this.cidtfdGrauParnt = cidtfdGrauParnt;
		}
		public String getRrlctoTtlarSbctr() {
			return rrlctoTtlarSbctr;
		}
		public void setRrlctoTtlarSbctr(String rrlctoTtlarSbctr) {
			this.rrlctoTtlarSbctr = rrlctoTtlarSbctr;
		}

    }
}
