/**********************************
 * FILE NAME: MP1Node.cpp
 *
 * DESCRIPTION: Membership protocol run by this Node.
 * 				Definition of MP1Node class functions.
 **********************************/

#include "MP1Node.h"
#include <sstream>

/**
 * Overloaded Constructor of the MP1Node class
 * You can add new members to the class if you think it
 * is necessary for your logic to work
 */
MP1Node::MP1Node(Member *member, Params *params, EmulNet *emul,
                 Log *log, Address *address) {
  for( int i = 0; i < 6; i++ ) {
    NULLADDR[i] = 0;
  }
  this->memberNode = member;
  this->emulNet = emul;
  this->log = log;
  this->par = params;
  this->memberNode->addr = *address;
}

/**
 * Destructor of the MP1Node class
 */
MP1Node::~MP1Node() {}

/**
 * FUNCTION NAME: recvLoop
 *
 * DESCRIPTION: This function receives message from the network and pushes into the queue
 *       	This function is called by a node to receive messages currently waiting for it
 */
int MP1Node::recvLoop() {
  if ( memberNode->bFailed ) {
    return false;
  }
  else {
    return emulNet->ENrecv(&(memberNode->addr), enqueueWrapper, NULL, 1, &(memberNode->mp1q));
  }
}

/**
 * FUNCTION NAME: enqueueWrapper
 *
 * DESCRIPTION: Enqueue the message from Emulnet into the queue
 */
int MP1Node::enqueueWrapper(void *env, char *buff, int size) {
  Queue q;
  return q.enqueue((queue<q_elt> *)env, (void *)buff, size);
}

/**
 * FUNCTION NAME: nodeStart
 *
 * DESCRIPTION: This function bootstraps the node
 * 				All initializations routines for a member.
 * 				Called by the application layer.
 */
void MP1Node::nodeStart(char *servaddrstr, short servport) {
  Address joinaddr;
  joinaddr = getJoinAddress();

  // Self booting routines
  if( initThisNode(&joinaddr) == -1 ) {
#ifdef DEBUGLOG
    log->LOG(&memberNode->addr, "init_thisnode failed. Exit.");
#endif
    exit(1);
  }

  if( !introduceSelfToGroup(&joinaddr) ) {
    finishUpThisNode();
#ifdef DEBUGLOG
    log->LOG(&memberNode->addr, "Unable to join self to group. Exiting.");
#endif
    exit(1);
  }

  return;
}

/**
 * FUNCTION NAME: initThisNode
 *
 * DESCRIPTION: Find out who I am and start up
 */
int MP1Node::initThisNode(Address *joinaddr) {
  /*
   * TODO This function is partially implemented and may require changes
   */
  // this->addr = anotherMember.addr;
  // this->myPos = anotherMember.myPos;
  // this->mp1q = anotherMember.mp1q;
  
  memberNode->bFailed = false;
  memberNode->inited  = true;
  memberNode->inGroup = false;
  
  // node is up!
  memberNode->nnb            = 0;
  memberNode->heartbeat      = 0;
  memberNode->pingCounter    = TFAIL;
  memberNode->timeOutCounter = -1;
  
  initMemberListTable(memberNode);

  return 0;
}

void MP1Node::sendMessage(MsgTypes type, Address *joinaddr) {
  int size;
  char* sendMsg = createMessage(type, &size);

#ifdef DEBUGLOG
    // stringstream smsg;
    // smsg << "send size: " << size;
    // log->LOG(&memberNode->addr, smsg.str().c_str());
#endif  
  
  emulNet->ENsend(&memberNode->addr,
                  joinaddr,
                  (char *)sendMsg,
                  size);
  free(sendMsg);
}

char* MP1Node::createMessage(MsgTypes type, int* totalSize) {
  
  // create header
  int headerSize     = sizeof(MessageHdr);
  MessageHdr* header = (MessageHdr*)malloc(headerSize);
  header->msgType    = type;
  header->id         = getId(memberNode->addr);
  header->port       = getPort(memberNode->addr);
  header->heartbeat  = memberNode->heartbeat;

  // create payload
  int entrySize   = sizeof(MessageEntryStruct);
  char* payload   = (char *)malloc(entrySize * memberNode->memberList.size());
  MessageEntryStruct* currentPayload = (MessageEntryStruct*)payload;
  int totalCountToSend = 0;
  
  for (MemberListEntry entry: memberNode->memberList) {
    // タイムアウトしている場合は送信しない
    if (isTimeout(entry) && (type == HEARTBEAT)) {
      
#ifdef DEBUGLOG
    // stringstream smsg;
    // smsg << "Detect Timeout id:" << entry.id;
    // smsg << " time:" << entry.timestamp << "/" << par->getcurrtime();
    // log->LOG(&memberNode->addr, smsg.str().c_str());
#endif
      continue;
    }
    
    MessageEntryStruct *entryStruct = createEntryStruct(entry);
    memcpy(currentPayload, entryStruct, entrySize);
    currentPayload++;
    totalCountToSend++;
  }

  // create message
  int payloadSize = totalCountToSend * entrySize;
  *totalSize = headerSize + payloadSize;
  
#ifdef DEBUGLOG
    // stringstream smsg;
    // smsg << " header size: " << headerSize;
    // smsg << " payload size: " << payloadSize;
    // smsg << " total size: " << *totalSize;
    // log->LOG(&memberNode->addr, smsg.str().c_str());
#endif
  
  char* message = (char *)malloc(*totalSize);
  memcpy(message, header, headerSize);
  memcpy(message + headerSize, payload, payloadSize);  
  
  free(header);
  free(payload);
  
  return message;
}

MessageEntryStruct *MP1Node::createEntryStruct(MemberListEntry entry) {
  MessageEntryStruct *mes
    = (MessageEntryStruct *)malloc(sizeof(MessageEntryStruct));
  memset(mes, 0, sizeof(MessageEntryStruct));
  
  mes->id        = entry.id;
  mes->port      = entry.port;
  mes->heartbeat = entry.heartbeat;
  mes->timestamp = entry.timestamp;
  return mes;
}

MemberListEntry *MP1Node::createMemberListEntry(MessageEntryStruct *mes) {
  MemberListEntry* entry = new MemberListEntry( mes->id,
                                                mes->port,
                                                mes->heartbeat,
                                                mes->timestamp);
  return entry;
}

/**
 * FUNCTION NAME: introduceSelfToGroup
 *
 * DESCRIPTION: Join the distributed system
 *              JONNREQを送信して、メンバリストに追加してもらう.
 */
int MP1Node::introduceSelfToGroup(Address *joinaddr) {
  
#ifdef DEBUGLOG
  static char s[1024];
#endif

  if ( 0 == strcmp((char *)&(memberNode->addr.addr), (char *)&(joinaddr->addr))) {
    // I am the group booter (first process to join the group). Boot up the group
    
#ifdef DEBUGLOG
    log->LOG(&memberNode->addr, "Starting up group...");
#endif
    
    memberNode->inGroup = true;

    // 自身をメンバシップリストに加える
    addNewEntry(getId(memberNode->addr),
                getPort(memberNode->addr), memberNode->heartbeat);
  }
  else {
    
#ifdef DEBUGLOG
    sprintf(s, "Trying to join...");
    log->LOG(&memberNode->addr, s);
#endif

    sendMessage(JOINREQ, joinaddr);
  }
  return 1;
}

/**
 * FUNCTION NAME: finishUpThisNode
 *
 * DESCRIPTION: Wind up this node and clean up state
 */
int MP1Node::finishUpThisNode(){
  /*
   * TODO Your code goes here
   */
  return 0;
}

/**
 * FUNCTION NAME: nodeLoop
 *
 * DESCRIPTION: Executed periodically at each member
 * 	        Check your messages in queue and perform membership protocol duties
 *              定期的にコールされる関数
 */
void MP1Node::nodeLoop() {
  if (memberNode->bFailed) {
    return;
  }

  // Check my messages
  checkMessages();

  // Wait until you're in the group...
  if(!memberNode->inGroup) {
    return;
  }

  // ...then jump in and share your responsibilites!
  nodeLoopOps();

  return;
}

/**
 * FUNCTION NAME: checkMessages
 *
 * DESCRIPTION: Check messages in the queue and call the respective message handler
 *              メッセージハンドラ.
 *              メッセージを定期的に受信する.
 *              メッセージを受信したら、コールバックを呼び出す.
 */
void MP1Node::checkMessages() {
  void *ptr;
  int  size;

  // Pop waiting messages from memberNode's mp1q
  while (!memberNode->mp1q.empty()) {
    ptr = memberNode->mp1q.front().elt;
    size = memberNode->mp1q.front().size;
    memberNode->mp1q.pop();
    recvCallBack((void *)memberNode, (char *)ptr, size);
  }
  return;
}

void MP1Node::addNewEntry(int id, short port, long heartbeat) {
  MemberListEntry entry = MemberListEntry(id, port,
                                          heartbeat,
                                          par->getcurrtime());
  memberNode->memberList.push_back(entry);
  memberNode->nnb++;

  Address addr = getAddress(id, port);
  log->logNodeAdd(&memberNode->addr, &addr);
}

void MP1Node::addNewEntry(MemberListEntry entry) {
  memberNode->memberList.push_back(entry);
  memberNode->nnb++;

  Address addr = getAddress(entry);
  log->logNodeAdd(&memberNode->addr, &addr);
}

/**
 * FUNCTION NAME: recvCallBack
 *
 * DESCRIPTION: Message handler for different message types
 */
bool MP1Node::recvCallBack(void *env, char *data, int size) {
  
#ifdef DEBUGLOG
  static char s[1024];
#endif
  
  MessageStruct *resvMsg        = (MessageStruct *)data;
  MessageHdr *resvHdr           = &resvMsg->header;
  MessageEntryStruct* currentEntry = (MessageEntryStruct *)(resvHdr + 1);
  Address joinaddr              = getAddress(resvHdr->id, resvHdr->port);
  int remainSize                = size - sizeof(MessageHdr);

#ifdef DEBUGLOG
  // int resvDataCount        = remainSize / sizeof(MessageEntryStruct);
  // stringstream smsg;
  // smsg << "size: " << size;
  // smsg << " / count: " << memberNode->memberList.size();
  // smsg << " / resv count: " << resvDataCount;
  // log->LOG(&memberNode->addr, smsg.str().c_str());
#endif  
  
  switch (resvHdr->msgType) {
  case JOINREQ: // 参加要求

#ifdef DEBUGLOG
  sprintf(s, "Received JOINREQ");
  log->LOG(&memberNode->addr, s);
#endif
    
    // 新しいノードを membership listに追加
    addNewEntry(resvHdr->id, resvHdr->port, resvHdr->heartbeat);

    // 送信元に応答する
    sendMessage(JOINREP, &joinaddr);
    break;
  case JOINREP: // 参加要求応答
    
#ifdef DEBUGLOG
  sprintf(s, "Received JOINREP");
  log->LOG(&memberNode->addr, s);
#endif
    
    memberNode->inGroup = true;
    updateMemberList(remainSize, currentEntry);
    
    break;
  case HEARTBEAT: // HeartBeat受信
    updateMemberList(remainSize, currentEntry);    
    break;
  case DUMMYLASTMSGTYPE:
    break;
  default:
    break;
  }
  return 0;
}

void MP1Node::updateMemberList(int remainSize,
                               MessageEntryStruct* currentEntry) {
  while (remainSize > 0) {
    MemberListEntry* entry = new MemberListEntry(currentEntry->id,
                                                 currentEntry->port,
                                                 currentEntry->heartbeat,
                                                 par->getcurrtime());
    updateMemberList(*entry);
    remainSize -= sizeof(MessageEntryStruct);
    currentEntry++;
    free(entry);
  }

}
void MP1Node::updateMemberList(MemberListEntry newEntry) {
  bool isMatched = false;

  vector<MemberListEntry>::iterator it;
  for (it = memberNode->memberList.begin();
       it != memberNode->memberList.end(); it++) {

    // 重複チェック
    if (isSameAddress(*it, newEntry)) {
      isMatched = true;

      // 更新が必要かチェック
      if (newEntry.getheartbeat() > it->getheartbeat()) {
        it->setheartbeat(newEntry.getheartbeat());
        it->settimestamp(par->getcurrtime());
      }
      break;
    }
  }

#ifdef DEBUGLOG
  // stringstream smsg;
  // smsg << "matched flag: " << isMatched;
  // log->LOG(&memberNode->addr, smsg.str().c_str());
#endif
  
  // 重複しているノードがなければ新規追加する.
  if (!isMatched) {
    addNewEntry(newEntry);
  }
}

/**
 * FUNCTION NAME: nodeLoopOps
 *
 * DESCRIPTION: Check if any node hasn't responded
 * within a timeout period and then delete
 * the nodes Propagate your membership list
 */
void MP1Node::nodeLoopOps() {
  memberNode->heartbeat++;
  // HeartBeat Self CountUp
  updateMemberList(MemberListEntry(getId(memberNode->addr),
                                   getPort(memberNode->addr),
                                   memberNode->heartbeat,
                                   par->getcurrtime()));
  
  // HeartBeatTimeout
  checkHeartBeatTimeout();

  // 他のノードへ通知
  sendHeartBeat();
}

/**
 * FUNCTION NAME: checkHeartBeatTimeout
 *
 * DESCRIPTION:
 * HeartBeatタイムアウトをしているノードがあるか調べる.
 * タイムアウトしている場合は、メンバシップリストから削除する
 */
void MP1Node::checkHeartBeatTimeout() {

  for (vector<MemberListEntry>::iterator it = memberNode->memberList.begin();
       it != memberNode->memberList.end(); it++) {

    // 時間になったらメンバシップリストから削除.
    if (shouldRemove(*it)) {
      Address addr = getAddress(*it);

      vector<MemberListEntry>::iterator next_it = it;
      vector<MemberListEntry>::iterator next_next_it = it+1;

      // 前に詰める.
      for (next_it = it;
           next_next_it != memberNode->memberList.end();
           next_it++, next_next_it++) {
        *next_it = *next_next_it;
      }

      // 再末尾を削除してリサイズ.
      memberNode->memberList.resize(memberNode->memberList.size()-1);
      it -= 1;
      
      log->logNodeRemove(&memberNode->addr, &addr);
    }
  }
}

/**
 * FUNCTION NAME: sendHeartBeart
 *
 * DESCRIPTION:
 * HeartBeatメッセージの送信
 */
void MP1Node::sendHeartBeat() {
  Address address;
  double prob = 0.3;

  for(MemberListEntry entry: memberNode->memberList) {
    // 自分自身はスキップ
    if (isSameAddress(getAddress(entry), memberNode->addr)) {
      continue;
    }

    // ランダムに送信する(Gossip)
    if ((((double)(rand() % 100))/100) < prob) {    
      address = getAddress(entry);
      sendMessage(HEARTBEAT, &address);
    }
  }
}

/**
 * FUNCTION NAME: isNullAddress
 *
 * DESCRIPTION: Function checks if the address is NULL
 */
int MP1Node::isNullAddress(Address *addr) {
  return (memcmp(addr->addr, NULLADDR, 6) == 0 ? 1 : 0);
}

/**
 * FUNCTION NAME: getJoinAddress
 *
 * DESCRIPTION: コーディネーターのアドレス取得
 */
Address MP1Node::getJoinAddress() {
  Address joinaddr;

  memset(&joinaddr, 0, sizeof(Address));
  *(int *)(&joinaddr.addr) = 1;
  *(short *)(&joinaddr.addr[4]) = 0;

  return joinaddr;
}

/**
 * FUNCTION NAME: initMemberListTable
 *
 * DESCRIPTION: Initialize the membership list
 */
void MP1Node::initMemberListTable(Member *memberNode) {
  memberNode->memberList.clear();
}

/**
 * FUNCTION NAME: printAddress
 *
 * DESCRIPTION: Print the Address
 */
void MP1Node::printAddress(Address *addr) {
  printf("%d.%d.%d.%d:%d \n",  addr->addr[0],addr->addr[1],addr->addr[2],
         addr->addr[3], *(short*)&addr->addr[4]) ;
}

// helper methods
Address MP1Node::getAddress(int id, short port) {
  Address address;
  memset(&address, 0, sizeof(Address));
  *(int *)(&address.addr) = id;
  *(short *)(&address.addr[4]) = port;
  return address;
}

Address MP1Node::getAddress(MemberListEntry entry) {
  return getAddress(entry.id, entry.port);
}

int MP1Node::getId(Address addr) {
  return *(int*)(&addr.addr[0]);
}

short MP1Node::getPort(Address addr) {
  return *(short*)(&addr.addr[4]);
}

bool MP1Node::isTimeout(MemberListEntry entry) {
  return (par->getcurrtime() - (int)entry.timestamp) > TFAIL;
}

bool MP1Node::shouldRemove(MemberListEntry entry) {
  return (par->getcurrtime() - (int)entry.timestamp) > TREMOVE;
}

bool MP1Node::isSameAddress(MemberListEntry x, MemberListEntry y) {
  return isSameAddress(getAddress(x), getAddress(y));
}

bool MP1Node::isSameAddress(Address x, Address y) {
  if (getId(x) != getId(y)) {
    return false;
  }
  if (getPort(x) != getPort(y)) {
    return false;
  }
  return true;
}
