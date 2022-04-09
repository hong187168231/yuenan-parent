package com.indo.game.pojo.dto.bti;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * reserve 相关服务请求参数对象
 * <Lines LineID="1377631862" Stake="25" OddsDec="2.11" Gain="184.27" LiveScore1="0" LiveScore2="0"
 *     HomeTeam="Estudiantes La Plata" AwayTeam="Velez Sarsfield" Status="Opened"
 *     EventState="" CustomerID="23164222" BetID="299729618355777537" BetTypeName="Trebles X 1 bet"
 *     LineTypeID="2" LineTypeName="Asian Handicap" RowTypeID="1" BranchID="1" BranchName="Soccer"
 *     LeagueID="2100713853" LeagueName="Copa Libertadores" CreationDate="2022-04-07T02:18:29"
 *     YourBet="Estudiantes La Plata -0.25" EventTypeID="0" EventTypeName="FT Asian Handicap"
 *     EventDate="2022-04-08T00:00:00" MasterEventID="1142051079" EventID="1142051079" NewMasterEventID="297166573804994560"
 *     NewEventID="297166573804994560" NewLeagueID="210310151096508416" NewLineID="0HC297166577219158016HMM"
 *     EventName="Estudiantes La Plata vs Velez Sarsfield" TeamMappingID="0" BetTypeID="2" Odds="111" Score="0:0"
 *     Points="-0.25" UserOddStyle="Decimal" OddsInUserStyle="2.11" SRIJbetCode="2101019999999" Country="International"
 *     Index="1" ReserveAmountType="Real" ReserveAmountTypeID="1"/>
 */
@XmlRootElement(name = "Lines")
public class BtiBetLineRequest {


    private String LineID;

    private String LiveScore1;

    private String LiveScore2;

    private String HomeTeam;

    private String AwayTeam;

    private String EventState;

    private String BetID;

    private String CustomerID;

    private String BetTypeName;

    private String LineTypeID;

    private String LineTypeName;

    private String RowTypeID;

    private String BranchID;

    private String BranchName;

    private String LeagueID;

    private String LeagueName;

    private Date CreationDate;

    private String YourBet;

    private String EventTypeID;

    private String EventTypeName;

    private String EventDate;

    private String MasterEventID;

    private String EventID;

    private String NewMasterEventID;

    private String NewEventID;

    private String NewLeagueID;

    private String NewLineID;

    private String EventName;

    private String TeamMappingID;

    private String BetTypeID;

    private String Odds;

    private String Score;

    private BigDecimal Points;

    private String UserOddStyle;

    private String OddsInUserStyle;

    private String SRIJbetCode;

    private String Country;

    private Integer Index;

    private String ReserveAmountType;

    private String ReserveAmountTypeID;

    private String Stake;

    private String OddsDec;

    private String Gain;

    private String Status;

    public String getLineID() {
        return LineID;
    }
    @XmlAttribute(name = "LineID")
    public void setLineID(String lineID) {
        LineID = lineID;
    }

    public String getLiveScore1() {
        return LiveScore1;
    }
    @XmlAttribute(name = "LiveScore1")
    public void setLiveScore1(String liveScore1) {
        LiveScore1 = liveScore1;
    }

    public String getLiveScore2() {
        return LiveScore2;
    }
    @XmlAttribute(name = "LiveScore2")
    public void setLiveScore2(String liveScore2) {
        LiveScore2 = liveScore2;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }
    @XmlAttribute(name = "HomeTeam")
    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return AwayTeam;
    }
    @XmlAttribute(name = "AwayTeam")
    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }

    public String getEventState() {
        return EventState;
    }
    @XmlAttribute(name = "EventState")
    public void setEventState(String eventState) {
        EventState = eventState;
    }

    public String getBetID() {
        return BetID;
    }
    @XmlAttribute(name = "BetID")
    public void setBetID(String betID) {
        BetID = betID;
    }

    public String getCustomerID() {
        return CustomerID;
    }
    @XmlAttribute(name = "CustomerID")
    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getBetTypeName() {
        return BetTypeName;
    }
    @XmlAttribute(name = "BetTypeName")
    public void setBetTypeName(String betTypeName) {
        BetTypeName = betTypeName;
    }

    public String getLineTypeID() {
        return LineTypeID;
    }
    @XmlAttribute(name = "LineTypeID")
    public void setLineTypeID(String lineTypeID) {
        LineTypeID = lineTypeID;
    }

    public String getLineTypeName() {
        return LineTypeName;
    }
    @XmlAttribute(name = "LineTypeName")
    public void setLineTypeName(String lineTypeName) {
        LineTypeName = lineTypeName;
    }

    public String getRowTypeID() {
        return RowTypeID;
    }
    @XmlAttribute(name = "RowTypeID")
    public void setRowTypeID(String rowTypeID) {
        RowTypeID = rowTypeID;
    }

    public String getBranchID() {
        return BranchID;
    }
    @XmlAttribute(name = "BranchID")
    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getBranchName() {
        return BranchName;
    }
    @XmlAttribute(name = "BranchName")
    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getLeagueID() {
        return LeagueID;
    }
    @XmlAttribute(name = "LeagueID")
    public void setLeagueID(String leagueID) {
        LeagueID = leagueID;
    }

    public String getLeagueName() {
        return LeagueName;
    }
    @XmlAttribute(name = "LeagueName")
    public void setLeagueName(String leagueName) {
        LeagueName = leagueName;
    }

    public Date getCreationDate() {
        return CreationDate;
    }
    @XmlAttribute(name = "CreationDate")
    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public String getYourBet() {
        return YourBet;
    }
    @XmlAttribute(name = "YourBet")
    public void setYourBet(String yourBet) {
        YourBet = yourBet;
    }

    public String getEventTypeID() {
        return EventTypeID;
    }
    @XmlAttribute(name = "EventTypeID")
    public void setEventTypeID(String eventTypeID) {
        EventTypeID = eventTypeID;
    }

    public String getEventTypeName() {
        return EventTypeName;
    }
    @XmlAttribute(name = "EventTypeName")
    public void setEventTypeName(String eventTypeName) {
        EventTypeName = eventTypeName;
    }

    public String getEventDate() {
        return EventDate;
    }
    @XmlAttribute(name = "EventDate")
    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getMasterEventID() {
        return MasterEventID;
    }
    @XmlAttribute(name = "MasterEventID")
    public void setMasterEventID(String masterEventID) {
        MasterEventID = masterEventID;
    }

    public String getEventID() {
        return EventID;
    }
    @XmlAttribute(name = "EventID")
    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getNewMasterEventID() {
        return NewMasterEventID;
    }
    @XmlAttribute(name = "NewMasterEventID")
    public void setNewMasterEventID(String newMasterEventID) {
        NewMasterEventID = newMasterEventID;
    }

    public String getNewEventID() {
        return NewEventID;
    }
    @XmlAttribute(name = "NewEventID")
    public void setNewEventID(String newEventID) {
        NewEventID = newEventID;
    }

    public String getNewLeagueID() {
        return NewLeagueID;
    }
    @XmlAttribute(name = "NewLeagueID")
    public void setNewLeagueID(String newLeagueID) {
        NewLeagueID = newLeagueID;
    }

    public String getNewLineID() {
        return NewLineID;
    }
    @XmlAttribute(name = "NewLineID")
    public void setNewLineID(String newLineID) {
        NewLineID = newLineID;
    }

    public String getEventName() {
        return EventName;
    }
    @XmlAttribute(name = "EventName")
    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getTeamMappingID() {
        return TeamMappingID;
    }
    @XmlAttribute(name = "TeamMappingID")
    public void setTeamMappingID(String teamMappingID) {
        TeamMappingID = teamMappingID;
    }

    public String getBetTypeID() {
        return BetTypeID;
    }
    @XmlAttribute(name = "BetTypeID")
    public void setBetTypeID(String betTypeID) {
        BetTypeID = betTypeID;
    }

    public String getOdds() {
        return Odds;
    }
    @XmlAttribute(name = "Odds")
    public void setOdds(String odds) {
        Odds = odds;
    }

    public String getScore() {
        return Score;
    }
    @XmlAttribute(name = "Score")
    public void setScore(String score) {
        Score = score;
    }

    public BigDecimal getPoints() {
        return Points;
    }
    @XmlAttribute(name = "Points")
    public void setPoints(BigDecimal points) {
        Points = points;
    }

    public String getUserOddStyle() {
        return UserOddStyle;
    }
    @XmlAttribute(name = "UserOddStyle")
    public void setUserOddStyle(String userOddStyle) {
        UserOddStyle = userOddStyle;
    }

    public String getOddsInUserStyle() {
        return OddsInUserStyle;
    }
    @XmlAttribute(name = "OddsInUserStyle")
    public void setOddsInUserStyle(String oddsInUserStyle) {
        OddsInUserStyle = oddsInUserStyle;
    }

    public String getSRIJbetCode() {
        return SRIJbetCode;
    }
    @XmlAttribute(name = "SRIJbetCode")
    public void setSRIJbetCode(String SRIJbetCode) {
        this.SRIJbetCode = SRIJbetCode;
    }

    public String getCountry() {
        return Country;
    }
    @XmlAttribute(name = "Country")
    public void setCountry(String country) {
        Country = country;
    }

    public Integer getIndex() {
        return Index;
    }
    @XmlAttribute(name = "Index")
    public void setIndex(Integer index) {
        Index = index;
    }

    public String getReserveAmountType() {
        return ReserveAmountType;
    }
    @XmlAttribute(name = "ReserveAmountType")
    public void setReserveAmountType(String reserveAmountType) {
        ReserveAmountType = reserveAmountType;
    }

    public String getReserveAmountTypeID() {
        return ReserveAmountTypeID;
    }
    @XmlAttribute(name = "ReserveAmountTypeID")
    public void setReserveAmountTypeID(String reserveAmountTypeID) {
        ReserveAmountTypeID = reserveAmountTypeID;
    }

    public String getStake() {
        return Stake;
    }
    @XmlAttribute(name = "Stake")
    public void setStake(String stake) {
        Stake = stake;
    }

    public String getOddsDec() {
        return OddsDec;
    }
    @XmlAttribute(name = "OddsDec")
    public void setOddsDec(String oddsDec) {
        OddsDec = oddsDec;
    }

    public String getGain() {
        return Gain;
    }
    @XmlAttribute(name = "Gain")
    public void setGain(String gain) {
        Gain = gain;
    }

    public String getStatus() {
        return Status;
    }
    @XmlAttribute(name = "Status")
    public void setStatus(String status) {
        Status = status;
    }

}
