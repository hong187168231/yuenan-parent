package com.indo.game.pojo.dto.bti;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <Selection LineID="1575412536" DecimalOdds="1.94" UserOddStyle="Decimal" OddsInUserStyle="1.94" BranchID="1"
 * BranchName="Soccer" LineTypeID="2" LineTypeName="Asian Handicap" LeagueID="2148154263" LeagueName="Japan - J-League 1"
 * HomeTeam="Sagan Tosu" AwayTeam="Consadole Sapporo" Score="0:0" YourBet="Sagan Tosu 0" EventTypeID="0" EventTypeName="FT Asian Handicap"
 * EventDateUTC="2022-04-06 10:00" MasterEventID="1693522599" EventID="1693522599" NewMasterEventID="298160210445615104"
 * NewEventID="298160210445615104" NewLeagueID="288320384594624512" NewLineID="0HC298160254041210880HMM" EncodedID="0HC298160254041210880HMM"
 * EventName="Sagan Tosu vs Consadole Sapporo" EventState="" BestOddsApplied="0" SelectionID="1" Index="0" IsLive="0"
 * SRIJbetCode="2101019999999" Country="International">
 */
@XmlRootElement(name = "Selection")
public class BtiCreditSelectionRequest {


    private String LineID;

    private BigDecimal DecimalOdds;

    private String UserOddStyle;

    private BigDecimal OddsInUserStyle;

    private String BranchID;

    private String BranchName;

    private String LineTypeID;

    private String LineTypeName;

    private String LeagueID;

    private String LeagueName;

    private String HomeTeam;

    private String AwayTeam;

    private String Score;

    private String YourBet;

    private String EventTypeID;

    private String EventTypeName;

    private String EventDateUTC;

    private String MasterEventID;

    private String EventID;

    private String NewMasterEventID;

    private String NewEventID;

    private String NewLeagueID;

    private String NewLineID;

    private String EncodedID;

    private String EventName;

    private String EventState;

    private String BestOddsApplied;

    private String Index;

    private String SelectionID;

    private String IsLive;

    private String SRIJbetCode;

    private String Country;


    private BtiChangesRequest changesRequest;

    public String getLineID() {
        return LineID;
    }
    @XmlAttribute(name = "LineID")
    public void setLineID(String lineID) {
        LineID = lineID;
    }

    public BigDecimal getDecimalOdds() {
        return DecimalOdds;
    }
    @XmlAttribute(name = "DecimalOdds")
    public void setDecimalOdds(BigDecimal decimalOdds) {
        DecimalOdds = decimalOdds;
    }

    public String getUserOddStyle() {
        return UserOddStyle;
    }
    @XmlAttribute(name = "UserOddStyle")
    public void setUserOddStyle(String userOddStyle) {
        UserOddStyle = userOddStyle;
    }

    public BigDecimal getOddsInUserStyle() {
        return OddsInUserStyle;
    }
    @XmlAttribute(name = "OddsInUserStyle")
    public void setOddsInUserStyle(BigDecimal oddsInUserStyle) {
        OddsInUserStyle = oddsInUserStyle;
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

    public String getScore() {
        return Score;
    }
    @XmlAttribute(name = "Score")
    public void setScore(String score) {
        Score = score;
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

    public String getEventDateUTC() {
        return EventDateUTC;
    }
    @XmlAttribute(name = "EventDateUTC")
    public void setEventDateUTC(String eventDateUTC) {
        EventDateUTC = eventDateUTC;
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

    public String getEncodedID() {
        return EncodedID;
    }
    @XmlAttribute(name = "EncodedID")
    public void setEncodedID(String encodedID) {
        EncodedID = encodedID;
    }

    public String getEventName() {
        return EventName;
    }
    @XmlAttribute(name = "EventName")
    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventState() {
        return EventState;
    }
    @XmlAttribute(name = "EventState")
    public void setEventState(String eventState) {
        EventState = eventState;
    }

    public String getBestOddsApplied() {
        return BestOddsApplied;
    }
    @XmlAttribute(name = "BestOddsApplied")
    public void setBestOddsApplied(String bestOddsApplied) {
        BestOddsApplied = bestOddsApplied;
    }

    public String getIndex() {
        return Index;
    }
    @XmlAttribute(name = "Index")
    public void setIndex(String index) {
        Index = index;
    }

    public String getSelectionID() {
        return SelectionID;
    }
    @XmlAttribute(name = "SelectionID")
    public void setSelectionID(String selectionID) {
        SelectionID = selectionID;
    }

    public String getIsLive() {
        return IsLive;
    }
    @XmlAttribute(name = "IsLive")
    public void setIsLive(String isLive) {
        IsLive = isLive;
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

    public BtiChangesRequest getChangesRequest() {
        return changesRequest;
    }
    @XmlElement(name = "Changes")
    public void setChangesRequest(BtiChangesRequest changesRequest) {
        this.changesRequest = changesRequest;
    }
}
